package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.WhiteNoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.lib.spline.SplineMapper;
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

import java.util.List;
import java.util.Set;

public class EarthZoneGenerator extends ZoneGenerator {
    private final RecyclingPool<NoiseCache> cache3DPool = new RecyclingPool<>(() -> null);
    private final RecyclingPool<NoiseCache> cache2DPool = new RecyclingPool<>(() -> null);

    public static int maxHeight = 255;
    public static int waterHeight = 100;
    public static int shoreHeight = 110;

    public static final int caveCeilingThickness = 8;
    public static final int lavaHeight = 15;

    private NoiseThreadColumn noiseThreads;

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
            new SplineMapper(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.5),
                    new SplinePoint(200, 0.2),
            }, SplineMapper.Interpolator.SMOOTHSTEP),
            List.of(
                    gravelBlock
            )
    );

    OreType ironOre = new OreType(
            getBlockStateInstance("base:ore_iron[default]"),
            4, 16,
            4, 16,
            new SplineMapper(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.9),
                    new SplinePoint(200, 0.7),
                    new SplinePoint(500, 0.4),
            }, SplineMapper.Interpolator.SMOOTHSTEP),
            List.of(
                    gabbroBlock
            )
    );

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
        noiseThreads = new NoiseThreadColumn();

        try {
            loadNoise();
        } catch (Exception e) {
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


            // Build 3d caches
            var noiseCache3Ds = new NoiseCache[Region.REGION_WIDTH];

            for(int i = 0; i < Region.REGION_WIDTH; i++) {
                var chunk = chunks[i];

                var cache3d = new NoiseCache(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ(), cache3DPool.get());
                noiseCache3Ds[i] = cache3d;
                noiseThreads.process(chunk, caveNoise, cache3d.getCache3d("cave"));
                noiseThreads.process(chunk, stoneTypeNoise, cache3d.getCache3d("stone_type"));
            }


            // Build 2d caches
            var cache2d = new NoiseCache(
                    col.getBlockX(),
                    col.chunkY * CHUNK_WIDTH,
                    col.getBlockZ(),
                    cache2DPool.get()
            );
            cache2d.buildCache2d("height", heightNoise);
            cache2d.cloneCache2d("height", "base_height");
            cache2d.buildCache2d("gradient", heightNoiseGradient);
            cache2d.buildCache2d("palette", paletteNoise);


            // Generate base terrain
            for(int i = Region.REGION_WIDTH - 1; i >= 0; i--) {
                generateChunk(chunks[i], cache2d, noiseCache3Ds[i]);
                generateOres(zone, chunks[i]);
            }

            // Generate features
            generateFeatures(zone, col, cache2d);


            // Recycle caches
            for(int i = 0; i < Region.REGION_WIDTH; i++) cache3DPool.recycle(noiseCache3Ds[i]);
            cache2DPool.recycle(cache2d);
        }
    }

    private void generateOres(Zone zone, Chunk chunk) {
        goldOre.populateChunk(zone, chunk, seed);
        ironOre.populateChunk(zone, chunk, seed);
    }

    private void generateChunk(Chunk chunk, NoiseCache noiseCache2d, NoiseCache noiseCache3d) {
        int globalX = chunk.blockX;
        int globalY = chunk.blockY + CHUNK_WIDTH - 1;
        int globalZ = chunk.blockZ;

        noiseThreads.waitForJobs(chunk);

        BlockState stoneTypeBlockState;

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double baseHeight = noiseCache2d.readCache2d("base_height", localX, localZ);
                double height = noiseCache2d.readCache2d("height", localX, localZ);
                double gradient = noiseCache2d.readCache2d("gradient", localX, localZ);
                double paletteOffset = noiseCache2d.readCache2d("palette", localX, localZ);

                for (int localY = CHUNK_WIDTH - 1; localY >= 0; localY--, globalY--) {
                    double caveDensity = noiseCache3d.readCache3d("cave", localX, localY, localZ);
                    double stoneType = noiseCache3d.readCache3d("stone_type", localX, localY, localZ);

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
                        if(globalY + 1 > height) noiseCache2d.writeCache2d("height", localX, localZ, --height);
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
                globalY += CHUNK_WIDTH;
            }
            globalZ -= CHUNK_WIDTH;
        }
    }

    private void generateFeatures(Zone zone, ChunkColumn column, NoiseCache noiseCache) {
        int globalX = column.getBlockX();
        int globalZ = column.getBlockZ();

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for (int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                var height = noiseCache.readCache2d("height", localX, localZ);
                var gradient = noiseCache.readCache2d("gradient", localX, localZ);
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
