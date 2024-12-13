package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.generator.WhiteNoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.lib.performance.Performance;
import com.arlojay.cosmicearth.lib.spline.Interpolator;
import com.arlojay.cosmicearth.lib.spline.SplinePoint;
import com.arlojay.cosmicearth.lib.threading.ThreadManager;
import com.arlojay.cosmicearth.worldgen.biome.BiomeSelector;
import com.arlojay.cosmicearth.worldgen.biome.impl.*;
import com.arlojay.cosmicearth.worldgen.mask.ChunkMask;
import com.arlojay.cosmicearth.worldgen.noise.NoiseCache2D;
import com.arlojay.cosmicearth.worldgen.noise.NoiseCache3D;
import com.arlojay.cosmicearth.worldgen.ore.OreType;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.threading.JobCreationHelper;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.savelib.blockdata.SingleBlockData;
import finalforeach.cosmicreach.savelib.blocks.IBlockDataFactory;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarthZoneGenerator extends ZoneGenerator {
    public static int maxHeight = 255;
    public static int waterHeight = 0;
    public static int shoreHeight = 10;

    public static final int caveCeilingThickness = 2;

    private ThreadManager noiseThreads;
    private ThreadManager oreThreads;

    public static final BiomeSelector biomeSelector = new BiomeSelector();
    private final Palettes blockPalette = new Palettes();

    private final OreType goldOre = new OreType(
            getBlockStateInstance("base:ore_gold[default]"),
            8, 16,
            4, 12,
            Interpolator.SMOOTHSTEP.create(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.5),
                    new SplinePoint(200, 0.2),
            }),
            List.of(
                    blockPalette.gravel
            )
    );

    private final OreType ironOre = new OreType(
            getBlockStateInstance("base:ore_iron[default]"),
            2, 8,
            12, 32,
            Interpolator.SMOOTHSTEP.create(new SplinePoint[]{
                    new SplinePoint(0, 1),
                    new SplinePoint(40, 0.9),
                    new SplinePoint(200, 0.7),
                    new SplinePoint(500, 0.4),
            }),
            List.of(
                    blockPalette.gabbro
            )
    );

    private final OreType[] oreTypes = new OreType[] { ironOre, goldOre };

    private final IBlockDataFactory<BlockState> chunkDataFactory = () -> new SingleBlockData<>(blockPalette.air);

    public NoiseNode heightNoise;
    public NoiseNode heightNoiseGradient;

    public static NoiseNode erosionBaseNoise;
    public static NoiseNode temperatureNoise;
    public static NoiseNode humidityNoise;
    public static NoiseNode continentalnessNoise;
    public static NoiseNode weirdnessNoise;
    public static NoiseNode peaksNoise;

    public NoiseNode paletteNoise;
    public NoiseNode caveNoise;
    public NoiseNode stoneTypeNoise;

    private void loadNoise() throws Exception {
        NoiseLoader.getProps().set("seed", seed);

        heightNoise = NoiseLoader.load("cosmicearth:height_noise");
        heightNoiseGradient = NoiseLoader.load("cosmicearth:height_noise_gradient");

        erosionBaseNoise = NoiseLoader.load("cosmicearth:factor/erosion");
        temperatureNoise = NoiseLoader.load("cosmicearth:factor/temperature");
        humidityNoise = NoiseLoader.load("cosmicearth:factor/humidity");
        continentalnessNoise = NoiseLoader.load("cosmicearth:factor/continentalness");
        weirdnessNoise = NoiseLoader.load("cosmicearth:factor/weirdness");
        peaksNoise = NoiseLoader.load("cosmicearth:factor/peaks");

        caveNoise = NoiseLoader.load("cosmicearth:cave_noise");
        stoneTypeNoise = NoiseLoader.load("cosmicearth:stone_type");

        paletteNoise = new WhiteNoiseGenerator(seed + 2);
    }

    @Override
    public void create() {
        noiseThreads = new ThreadManager(4);
        oreThreads = new ThreadManager(2);

        biomeSelector.registerBiome(new PlainsBiome(
                seed,
                new Range(0.3, 0.6),
                new Range(-0.2, 0.3),
                new Range(0.2, 1.0),
                new Range(0.3, 0.5)
        ));
        biomeSelector.registerBiome(new ForestBiome(
                seed,
                new Range(-0.2, 0.3),
                new Range(0.4, 0.8),
                new Range(-0.1, 1.0),
                new Range(0.1, 0.3)
        ));
        biomeSelector.registerBiome(new DesertBiome(
                seed,
                new Range(0.5, 1.0),
                new Range(-1.0, -0.5),
                new Range(0.7d, 1.0d),
                new Range(0.6d, 1.0d)
        ));
        biomeSelector.registerBiome(new ConiferousForestBiome(
                seed,
                new Range(-0.2, 0.35),
                new Range(0.2, 0.5),
                new Range(-0.3d, 0.3d),
                new Range(0.3d, 1.0d)
        ));
        biomeSelector.registerBiome(new SnowyPlainsBiome(
                seed,
                new Range(-1.0, -0.45),
                new Range(0.35, 0.68),
                new Range(-0.25d, 0.25d),
                new Range(0.0d, 1.0d)
        ));
        biomeSelector.registerBiome(new TundraBiome(
                seed,
                new Range(-1.0, -0.45),
                new Range(-1.0, 0.35),
                new Range(-0.25d, 0.25d),
                new Range(0.0d, 1.0d)
        ));
        biomeSelector.registerBiome(new SnowyConiferousForestBiome(
                seed,
                new Range(-1.0d, -0.45d),
                new Range(0.65d, 1.0d),
                new Range(-0.25d, 0.25d),
                new Range(0.2d, 1.0d)
        ));
        biomeSelector.registerBiome(new TropicalShoreBiome(
                seed,
                new Range(0.5, 1.0),
                new Range(0.77, 1.0),
                new Range(-1.0, 1.0),
                new Range(-0.05d, 0.1d)
        ));
        biomeSelector.registerBiome(new ShoreBiome(
                seed,
                new Range(-0.3, 0.5),
                new Range(-1.0, 0.77),
                new Range(-1.0, 1.0),
                new Range(-0.05d, 0.1d)
        ));
        biomeSelector.registerBiome(new ColdShoreBiome(
                seed,
                new Range(-1.0, -0.3),
                new Range(-1.0, 0.77),
                new Range(-1.0, 1.0),
                new Range(-0.05d, 0.1d)
        ));
        biomeSelector.registerBiome(new OceanBiome(
                seed,
                new Range(-1.0, 1.0),
                new Range(-1.0, 1.0),
                new Range(-1.0, 1.0),
                new Range(-1.0d, -0.05d)
        ));

        try {
            loadNoise();
        } catch (Exception e) {
            if(e instanceof NoiseNode) return;
            e.printStackTrace();
        }

    }

    private int c = 0;
    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn col) {
        c++;
        if(c > 100) {
            c = 0;
            Performance.report();
        }
        if (col.chunkY * CHUNK_WIDTH <= maxHeight) {
            var chunks = new Chunk[Region.REGION_WIDTH];

            // Initialize chunks
            for(int i = 0; i < Region.REGION_WIDTH; i++) {
                Performance.start("Initialize Chunk");
                int chunkY = col.chunkY + i;

                Chunk chunk = zone.getChunkAtChunkCoords(col.chunkX, chunkY, col.chunkZ);

                if (chunk == null) {
                    chunk = new Chunk(col.chunkX, chunkY, col.chunkZ);
                    chunk.initChunkData(chunkDataFactory);

                    col.addChunk(chunk);
                    zone.addChunk(chunk);
                }

                chunks[i] = chunk;
                Performance.end("Initialize Chunk");
            }


            var noiseCache3Ds = new NoiseCache3D[Region.REGION_WIDTH];
            var oreBlockUpdates = new ArrayList<Map<OreType, ChunkMask>>(Region.REGION_WIDTH);

            for(int i = 0; i < Region.REGION_WIDTH; i++) {
                var chunk = chunks[i];

                Performance.start("Build 3D Caches");
                // Build 3d caches
                {
                    var cache3d = NoiseCache3D.create(chunk.getBlockX(), chunk.getBlockY(), chunk.getBlockZ());
                    noiseCache3Ds[i] = cache3d;

                    var thread = noiseThreads.getThread();
                    noiseThreads.addJob(thread, chunk, JobCreationHelper.createNoiseJob(caveNoise, thread, chunk, cache3d.getCache("cave")));
                    noiseThreads.addJob(thread, chunk, JobCreationHelper.createNoiseJob(stoneTypeNoise, thread, chunk, cache3d.getCache("stone_type")));
                }
                Performance.end("Build 3D Caches");


                Performance.start("Build ore caches");
                // Build ore caches
                {
                    var thread = oreThreads.getThread();
                    var blockUpdates = new HashMap<OreType, ChunkMask>();

                    for (var oreType : oreTypes) {
                        var mask = ChunkMask.create();
                        var job = JobCreationHelper.createOregenJob(chunk, oreType, seed, mask);

                        oreThreads.addJob(thread, chunk, job);
                        blockUpdates.put(oreType, mask);
                    }

                    oreBlockUpdates.add(blockUpdates);
                }
                Performance.end("Build ore caches");
            }


            Performance.start("Build 2d caches");
            // Build 2d caches
            var cache2d = NoiseCache2D.create(col.getBlockX(), col.getBlockZ());
            cache2d.build("height", heightNoise);
            cache2d.copyCache("height", "base_height");
            cache2d.build("gradient", heightNoiseGradient);
            cache2d.build("palette", paletteNoise);

            cache2d.build("erosion", erosionBaseNoise);
            cache2d.build("temperature", temperatureNoise);
            cache2d.build("humidity", humidityNoise);
            cache2d.build("continentalness", continentalnessNoise);

            Performance.end("Build 2d caches");


            // Generate base terrain
            for(int i = Region.REGION_WIDTH - 1; i >= 0; i--) {
                Performance.start("Generate base terrain");
                generateChunk(chunks[i], cache2d, noiseCache3Ds[i]);
                Performance.end("Generate base terrain");

                Performance.start("Generate ores");
                generateOres(chunks[i], oreBlockUpdates.get(i));
                Performance.end("Generate ores");
            }

            // Generate features
            Performance.start("Generate features");
            generateFeatures(zone, col, cache2d);
            Performance.end("Generate features");


            // Recycle caches
            Performance.start("Recycle caches");
            for(int i = 0; i < Region.REGION_WIDTH; i++) noiseCache3Ds[i].recycle();
            cache2d.recycle();
            Performance.end("Recycle caches");
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

        Performance.start("Wait for jobs");
        noiseThreads.waitForJobs(chunk);
        Performance.end("Wait for jobs");

        BlockState stoneTypeBlockState;

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double baseHeight = noiseCache2d.read("base_height", localX, localZ);
                double height = noiseCache2d.read("height", localX, localZ);
                double gradient = noiseCache2d.read("gradient", localX, localZ);

                double continentalness = noiseCache2d.read("continentalness", localX, localZ);
                double temperature = noiseCache2d.read("temperature", localX, localZ);
                double humidity = noiseCache2d.read("humidity", localX, localZ);
                double erosion = noiseCache2d.read("erosion", localX, localZ);

                var biome = biomeSelector.getBiome(temperature, humidity, erosion, continentalness);


                for (int localY = CHUNK_WIDTH - 1; localY >= 0; localY--, globalY--) {
                    if(globalY > baseHeight) {
                        if(globalY <= waterHeight) {
                            chunk.setBlockState(blockPalette.water, localX, localY, localZ);
                        }
                        continue;
                    }

                    double caveDensity = noiseCache3d.read("cave", localX, localY, localZ);
                    double stoneType = noiseCache3d.read("stone_type", localX, localY, localZ);

                    stoneTypeBlockState = blockPalette.stoneType.getItem(stoneType);

                    boolean voidCaves = globalY > baseHeight - caveCeilingThickness && globalY < shoreHeight;
                    if(caveDensity < 0.0 && !voidCaves) {
                        if(globalY + 1 > height) height--;
                        continue;
                    }

                    boolean isBeach = (continentalness < 0.0 || globalY < waterHeight);

                    // Grass/top layer
                    if(globalY > height - 1) {
                        BlockState topsoil;

                        // Beach/underwater override
                        if(isBeach && globalY > baseHeight - 1) {
                            topsoil = gradient < 2d ? blockPalette.sand : stoneTypeBlockState;
                        } else {
                            topsoil = biome.getTopsoil(globalX, globalY, globalZ, gradient);
                        }
                        chunk.setBlockState(topsoil, localX, localY, localZ);
                        continue;
                    }

                    // Dirt/middle layer
                    if(globalY > height - 5) {
                        BlockState loam;

                        // Beach/underwater override
                        if(isBeach && globalY > baseHeight - 5) {
                            // Shallow gradient
                            if(gradient < 1.5) {
                                loam = gradient < 0.9 ? blockPalette.sand : blockPalette.gravel;
                            } else {
                                loam = stoneTypeBlockState;
                            }
                        } else {
                            loam = biome.getLoam(globalX, globalY, globalZ, gradient);
                        }
                        chunk.setBlockState(loam, localX, localY, localZ);
                        continue;
                    }

                    chunk.setBlockState(stoneTypeBlockState, localX, localY, localZ);
                }
                noiseCache2d.write("height", localX, localZ, height);
                globalY += CHUNK_WIDTH;
            }
            globalZ -= CHUNK_WIDTH;
        }
    }

    private void generateFeatures(Zone zone, ChunkColumn column, NoiseCache2D noiseCache2d) {
        int globalX = column.getBlockX();
        int globalZ = column.getBlockZ();

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for (int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double height = noiseCache2d.read("height", localX, localZ);

                int globalY = (int) Math.ceil(height);
                var ground = zone.getBlockState(globalX, globalY - 1, globalZ);
                var air = zone.getBlockState(globalX, globalY, globalZ);

                if(ground == null || air == null) continue;



                double gradient = noiseCache2d.read("gradient", localX, localZ);

                double temperature = noiseCache2d.read("temperature", localX, localZ);
                double humidity = noiseCache2d.read("humidity", localX, localZ);
                double erosion = noiseCache2d.read("erosion", localX, localZ);
                double continentalness = noiseCache2d.read("continentalness", localX, localZ);

                var biome = biomeSelector.getBiome(temperature, humidity, erosion, continentalness);

                var structure = biome.getStructure(zone, globalX, globalY, globalZ, ground, air, gradient);
                if(structure == null) continue;

                structure.worldgenStructure.generate(seed, zone, globalX, globalY, globalZ);
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
        return Integer.MIN_VALUE;
    }
}
