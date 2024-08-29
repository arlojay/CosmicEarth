package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.WhiteNoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.lib.spline.Interpolator;
import com.arlojay.cosmicearth.lib.spline.SplinePoint;
import com.arlojay.cosmicearth.lib.variety.GroupedPalette;
import com.arlojay.cosmicearth.lib.variety.PaletteItem;
import com.arlojay.cosmicearth.lib.variety.RandomPalette;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.savelib.blockdata.SingleBlockData;
import finalforeach.cosmicreach.savelib.blocks.IBlockDataFactory;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import finalforeach.cosmicreach.worldgen.trees.CoconutTree;

import java.util.*;

public class EarthZoneGenerator extends ZoneGenerator {
    public static int maxHeight = 255;
    public static int waterHeight = 100;
    public static int shoreHeight = 110;

    public static final int caveCeilingThickness = 8;
    public static final int lavaHeight = 15;

    private NoiseThreadColumn noiseThreads;
    private NoiseThreadColumn oreThreads;

    BlockState airBlock = this.getBlockStateInstance("base:air[default]");
    BlockState stoneBlock = this.getBlockStateInstance("base:stone_basalt[default]");
    BlockState gabbroBlock = this.getBlockStateInstance("base:stone_gabbro[default]");
    BlockState limestoneBlock = this.getBlockStateInstance("base:stone_limestone[default]");
    BlockState grassBlock = this.getBlockStateInstance("base:grass[default]");
    BlockState fullGrassBlock = this.getBlockStateInstance("base:grass[type=full]");
    BlockState dirtBlock = this.getBlockStateInstance("base:dirt[default]");
    BlockState sandBlock = this.getBlockStateInstance("base:sand[default]");
    BlockState waterBlock = this.getBlockStateInstance("base:water[default]");
    BlockState gravelBlock = this.getBlockStateInstance("base:stone_gravel[default]");
    BlockState shortGrassBlock = this.getBlockStateInstance("cosmicearth:short_grass[default]");
    BlockState tallGrassBlock = this.getBlockStateInstance("cosmicearth:tall_grass[default]");
    BlockState magmaBlock = this.getBlockStateInstance("base:magma[default]");

    RandomPalette<BlockState> steepGradientTopsoil = new RandomPalette<>(Set.of(
            new PaletteItem<>(grassBlock, 1d),
            new PaletteItem<>(gravelBlock, 1d),
            new PaletteItem<>(gabbroBlock, 2d)
    ));
    GroupedPalette<BlockState> stoneTypePalette = new GroupedPalette<>(Set.of(
            new PaletteItem<>(gabbroBlock, -1d),
            new PaletteItem<>(gravelBlock, 1d)
    ));

    OreType goldOre = new OreType(
            getBlockStateInstance("base:ore_gold[default]"),
            8, 16,
            4, 12,
            Interpolator.SMOOTHSTEP.create(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.5),
                    new SplinePoint(200, 0.2),
            }),
            List.of(
                    gravelBlock
            )
    );

    OreType ironOre = new OreType(
            getBlockStateInstance("base:ore_iron[default]"),
            4, 16,
            4, 16,
            Interpolator.SMOOTHSTEP.create(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.9),
                    new SplinePoint(200, 0.7),
                    new SplinePoint(500, 0.4),
            }),
            List.of(
                    gabbroBlock
            )
    );

    OreType[] oreTypes = new OreType[] { ironOre, goldOre };

    WorldgenStructure pineTreeStructure = new PineTreeStructure();
    WorldgenStructure floraClusterStructure = new FloraClusterStructure();
    WorldgenStructure oakTreeStructure = new OakTreeStructure();

    private final IBlockDataFactory<BlockState> chunkDataFactory = () -> new SingleBlockData<>(airBlock);

    private NoiseNode heightNoise;
    private NoiseNode heightNoiseGradient;
    private NoiseNode paletteNoise;
    private NoiseNode featureNoise;
    private NoiseNode caveNoise;
    private NoiseNode stoneTypeNoise;

    private void loadNoise() throws Exception {

        NoiseLoader.getProps().set("seed", seed);

        heightNoise = NoiseLoader.loadById("cosmicearth:height_noise");
        heightNoiseGradient = NoiseLoader.loadById("cosmicearth:height_noise_gradient");
        caveNoise = NoiseLoader.loadById("cosmicearth:cave_noise");
        stoneTypeNoise = NoiseLoader.loadById("cosmicearth:stone_type");

        paletteNoise = new WhiteNoiseGenerator(seed + 2);
        featureNoise = new WhiteNoiseGenerator(seed + 6);
    }

    @Override
    public void create() {
        noiseThreads = new NoiseThreadColumn(4);
        oreThreads = new NoiseThreadColumn(2);

        try {
            loadNoise();
        } catch (Exception e) {
            if(e instanceof NoiseNode) return;
            e.printStackTrace();
        }
    }

    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn col) {
        if (col.chunkY >= 0 && col.chunkY <= 15) {
            var chunks = new Chunk[Region.REGION_WIDTH];

            // Initialize chunks
            for(int i = 0; i < Region.REGION_WIDTH; i++) {
                int chunkY = col.chunkY + i;

                Chunk chunk = zone.getChunkAtChunkCoords(col.chunkX, chunkY, col.chunkZ);

                if (chunk == null) {
                    chunk = new Chunk(col.chunkX, chunkY, col.chunkZ);
                    chunk.initChunkData(chunkDataFactory);

                    col.addChunk(chunk);
                    zone.addChunk(chunk);
                }

                chunks[i] = chunk;
            }


            var noiseCache3Ds = new NoiseCache3D[Region.REGION_WIDTH];
            var oreBlockUpdates = new ArrayList<Map<OreType, ChunkMask>>(Region.REGION_WIDTH);

            for(int i = 0; i < Region.REGION_WIDTH; i++) {
                var chunk = chunks[i];

                // Build 3d caches
                {
                    var cache3d = NoiseCache3D.create(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ());
                    noiseCache3Ds[i] = cache3d;

                    var thread = noiseThreads.getThread();
                    noiseThreads.addJob(thread, chunk, JobCreationHelpler.createNoiseJob(caveNoise, thread, chunk, cache3d.getCache("cave")));
                    noiseThreads.addJob(thread, chunk, JobCreationHelpler.createNoiseJob(stoneTypeNoise, thread, chunk, cache3d.getCache("stone_type")));
                }


                // Build ore caches
                {
                    var thread = oreThreads.getThread();
                    var blockUpdates = new HashMap<OreType, ChunkMask>();

                    for (var oreType : oreTypes) {
                        var mask = ChunkMask.create();
                        var job = JobCreationHelpler.createOregenJob(chunk, oreType, seed, mask);

                        oreThreads.addJob(thread, chunk, job);
                        blockUpdates.put(oreType, mask);
                    }

                    oreBlockUpdates.add(blockUpdates);
                }
            }


            // Build 2d caches
            var cache2d = NoiseCache2D.create(col.getBlockX(), col.getBlockZ());
            cache2d.build("height", heightNoise);
            cache2d.copyCache("height", "base_height");
            cache2d.build("gradient", heightNoiseGradient);
            cache2d.build("palette", paletteNoise);


            // Generate base terrain
            for(int i = Region.REGION_WIDTH - 1; i >= 0; i--) {
                generateChunk(chunks[i], cache2d, noiseCache3Ds[i]);
                generateOres(chunks[i], oreBlockUpdates.get(i));
            }

            // Generate features
            generateFeatures(zone, col, cache2d);


            // Recycle caches
            for(int i = 0; i < Region.REGION_WIDTH; i++) noiseCache3Ds[i].recycle();
            cache2d.recycle();
        }
    }

    private void generateOres(Chunk chunk, Map<OreType, ChunkMask> updateList) {
        oreThreads.waitForJobs(chunk);

        for(var oreType : oreTypes) {
            var mask = updateList.get(oreType);
            var replaceMask = oreType.getReplaceMask();

            int i = 0;
            for (int blockX = 0; blockX < CHUNK_WIDTH; blockX++) {
                for (int blockY = 0; blockY < CHUNK_WIDTH; blockY++) {
                    for (int blockZ = 0; blockZ < CHUNK_WIDTH; blockZ++) {
                        if(!replaceMask.contains(chunk.getBlockState(blockX, blockY, blockZ))) continue;

                        if(mask.mask[i]) chunk.setBlockState(oreType.getBlock(), blockX, blockY, blockZ);
                        i++;
                    }
                }
            }

            mask.recycle();
        }
    }

    private void generateChunk(Chunk chunk, NoiseCache2D noiseCache2d, NoiseCache3D noiseCache3d) {
        int globalX = chunk.blockX;
        int globalY = chunk.blockY + CHUNK_WIDTH - 1;
        int globalZ = chunk.blockZ;

        noiseThreads.waitForJobs(chunk);

        BlockState stoneTypeBlockState;

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double baseHeight = noiseCache2d.read("base_height", localX, localZ);
                double height = noiseCache2d.read("height", localX, localZ);
                double gradient = noiseCache2d.read("gradient", localX, localZ);
                double paletteOffset = noiseCache2d.read("palette", localX, localZ);

                for (int localY = CHUNK_WIDTH - 1; localY >= 0; localY--, globalY--) {
                    double caveDensity = noiseCache3d.read("cave", localX, localY, localZ);
                    double stoneType = noiseCache3d.read("stone_type", localX, localY, localZ);

                    stoneTypeBlockState = stoneTypePalette.getItem(stoneType);

                    if(globalY > baseHeight) {
                        if(globalY <= waterHeight) {
                            chunk.setBlockState(waterBlock, localX, localY, localZ);
                        }
                        continue;
                    }

                    boolean voidCaves = globalY < shoreHeight && globalY > baseHeight - caveCeilingThickness;
                    if(caveDensity < 0.0 && !voidCaves) {
                        if(globalY < lavaHeight) chunk.setBlockState(magmaBlock, localX, localY, localZ);
                        if(globalY + 1 > height) height--;
                        continue;
                    }

                    // Grass/top layer
                    if(globalY > height - 1) {
                        // Shallow gradient
                        if(gradient < 0.9) {
                            if(baseHeight > shoreHeight) {
                                // Inland
                                chunk.setBlockState(gradient < 0.4 ? fullGrassBlock : grassBlock, localX, localY, localZ);
                                continue;
                            } else {
                                // Shoreline / underwater
                                chunk.setBlockState(gradient < 0.7 ? sandBlock : gravelBlock, localX, localY, localZ);
                                continue;
                            }
                        }
                        // Steep gradient
                        else {
                            if(baseHeight > shoreHeight) {
                                // Inland
                                var paletteBlock = steepGradientTopsoil.getItem(
                                        Double.doubleToLongBits(paletteOffset * Double.MAX_VALUE)
                                );
                                chunk.setBlockState(paletteBlock, localX, localY, localZ);
                                continue;
                            } else {
                                // Shoreline / underwater
                                chunk.setBlockState(stoneTypeBlockState, localX, localY, localZ);
                                continue;
                            }
                        }
                    }

                    // Dirt/middle layer
                    if(globalY > height - 5) {
                        // Shallow gradient
                        if(gradient < 1.0) {
                            // Inland
                            if(baseHeight > shoreHeight) {
                                chunk.setBlockState(dirtBlock, localX, localY, localZ);
                                continue;
                            }
                            // Shoreline / underwater
                            else {
                                chunk.setBlockState(gradient < 0.9 ? sandBlock : gravelBlock, localX, localY, localZ);
                                continue;
                            }
                        }
                        // Steep gradient
                        else {
                            // Inland
                            if(baseHeight > shoreHeight) {
                                chunk.setBlockState(stoneTypeBlockState, localX, localY, localZ);
                                continue;
                            }
                            // Shoreline / underwater
                            else {
                                chunk.setBlockState(stoneTypeBlockState, localX, localY, localZ);
                                continue;
                            }
                        }
                    }

                    chunk.setBlockState(stoneTypeBlockState, localX, localY, localZ);
                }
                noiseCache2d.write("height", localX, localZ, height);
                globalY += CHUNK_WIDTH;
            }
            globalZ -= CHUNK_WIDTH;
        }
    }

    private void generateFeatures(Zone zone, ChunkColumn column, NoiseCache2D noiseCache) {
        int globalX = column.getBlockX();
        int globalZ = column.getBlockZ();

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for (int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                var height = noiseCache.read("height", localX, localZ);
                var gradient = noiseCache.read("gradient", localX, localZ);
                var featureValue = featureNoise.sample(globalX, globalZ);

                var globalY = (int) Math.round(height + 0.5);
                var ground = zone.getBlockState(globalX, globalY - 1, globalZ);
                var air = zone.getBlockState(globalX, globalY, globalZ);

                if(ground == null || air == null) continue;

                // Tree generation
                if(
                        featureValue > 0.98 && (
                                ground.equals(grassBlock) ||
                                ground.equals(dirtBlock) ||
                                ground.equals(sandBlock)
                        ) &&
                        air.equals(airBlock)
                ) {
                    // Coconut trees
                    if(
                            height < waterHeight + 24d &&
                            height > 48d &&
                            gradient < 0.2d
                    ) {
                        CoconutTree.generateTree(this.seed, zone, globalX, globalY, globalZ);
                        continue;
                    }
                    // Pine trees
                    else if(
                            height > shoreHeight + 10d &&
                            height < maxHeight - 48d &&
                            gradient < 0.6d
                    ) {
                        pineTreeStructure.generate(this.seed, zone, globalX, globalY, globalZ);
                        continue;
                    }
                    // Oak trees
                    else if(featureValue > 0.996) {
                        oakTreeStructure.generate(this.seed, zone, globalX, globalY, globalZ);
                        continue;
                    }
                }

                if(
                        featureValue > -0.5 && (
                                ground.equals(grassBlock) ||
                                ground.equals(fullGrassBlock)
                        ) &&
                        air.equals(airBlock)
                ) {
                    zone.setBlockState(featureValue > 0.0 ? tallGrassBlock : shortGrassBlock, globalX, globalY, globalZ);
                    continue;
                }

                if(
                        featureValue > -0.55 && (
                                ground.equals(grassBlock) ||
                                ground.equals(fullGrassBlock)
                        ) &&
                        air.equals(airBlock)
                ) {
                    floraClusterStructure.generate(this.seed, zone, globalX, globalY, globalZ);
                }
            }
            globalZ -= CHUNK_WIDTH;
        }
    }

    @Override
    public String getSaveKey() {
        return CosmicEarthMod.MOD_ID + ":earth";
    }

    @Override
    protected String getName() {
        return "Earth";
    }

    @Override
    public int getDefaultRespawnYLevel() {
        return 0;
    }
}
