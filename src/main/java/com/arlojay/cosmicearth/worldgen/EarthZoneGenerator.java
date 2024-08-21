package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.*;
import com.arlojay.cosmicearth.lib.spline.SplineMapper;
import com.arlojay.cosmicearth.lib.spline.SplinePoint;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.savelib.blockdata.SingleBlockData;
import finalforeach.cosmicreach.savelib.blocks.IBlockDataFactory;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;
import finalforeach.cosmicreach.worldgen.trees.CoconutTree;

public class EarthZoneGenerator extends ZoneGenerator {
    public static int maxHeight = 255;
    public static int waterHeight = 100;
    public static int shoreHeight = 110;

    BlockState airBlock = this.getBlockStateInstance("base:air[default]");
    BlockState stoneBlock = this.getBlockStateInstance("base:stone_basalt[default]");
    BlockState grassBlock = this.getBlockStateInstance("base:grass[default]");
    BlockState fullGrassBlock = this.getBlockStateInstance("base:grass[type=full]");
    BlockState dirtBlock = this.getBlockStateInstance("base:dirt[default]");
    BlockState sandBlock = this.getBlockStateInstance("base:sand[default]");
    BlockState waterBlock = this.getBlockStateInstance("base:water[default]");
    BlockState gravelBlock = this.getBlockStateInstance("base:stone_gravel[default]");
    BlockState shortGrassBlock = this.getBlockStateInstance("cosmicearth:short_grass[default]");
    BlockState tallGrassBlock = this.getBlockStateInstance("cosmicearth:tall_grass[default]");

    WorldgenStructure pineTreeStructure = new PineTreeStructure();
    WorldgenStructure floraClusterStructure = new FloraClusterStructure();
    WorldgenStructure oakTreeStructure = new OakTreeStructure();

    private final IBlockDataFactory<BlockState> chunkDataFactory = () -> new SingleBlockData<>(airBlock);

    private NoiseNode heightNoise;
    private NoiseNode heightNoiseGradient;
    private NoiseNode paletteNoise;
    private NoiseNode featureNoise;

    @Override
    public void create() {
        var shapedNoise = new NoiseMapper(
                new NoiseSpline(
                        new NoiseMixer(new NoiseMixer.MixerSource[]{
                                new NoiseMixer.MixerSource(
                                        new OctaveNoise(
                                                new SimplexNoiseGenerator(seed),
                                                10,
                                                0.4,
                                                2.0,
                                                0.3
                                        ),
                                        10d
                                ),
                                new NoiseMixer.MixerSource(
                                        new NoiseScaler(
                                                new SimplexNoiseGenerator(seed + 1),
                                                8d
                                        ),
                                        1d),
                        }),
                        new SplineMapper(new SplinePoint[]{
                                new SplinePoint(-1, -1),
                                new SplinePoint(-0.5, -0.4),
                                new SplinePoint(0, 0),
                                new SplinePoint(0.5, 0.2),
                                new SplinePoint(0.8, 0.6),
                                new SplinePoint(1, 1)
                        })
                ),
                60d, 140d
        );

        var ridgesNoise = new NoiseMapper(
                new NoiseSpline(
                        new OctaveNoise(
                                new SimplexNoiseGenerator(seed + 3),
                                3,
                                0.5,
                                2.0,
                                0.0
                        ),
                        new SplineMapper(new SplinePoint[]{
                                new SplinePoint(   -1d,     0d  ),
                                new SplinePoint(   -2/3d,   1d  ),
                                new SplinePoint(    0d,    -1d  ),
                                new SplinePoint(    2/3d,   1d  ),
                                new SplinePoint(    1d,     0d  ),
                        })
                ),
                -40d, 100d
        );

        var erosionNoise = new ErodedNoise(
                new SimplexNoiseGenerator(seed + 4),
                3,
                2.0,
                0.7
        );

        heightNoise = new NoiseMixer(new NoiseMixer.MixerSource[]{
                new NoiseMixer.MixerSource(
                        new NoiseScaler(shapedNoise, 0.002d),
                        1d
                ),
                new NoiseMixer.MixerSource(
                        new NoiseScaler(ridgesNoise, 0.0003d),
                        0.6d
                ),
                new NoiseMixer.MixerSource(
                        new NoiseScaler(erosionNoise, 0.004d),
                        1d
                )
        }, false);

//        heightNoise = new NoiseScaler(
//                new NoiseMapper(
//                        new ErodedNoise(new SimplexNoiseGenerator(seed + 4), 2, 2.0, 0.5),
//                        70d, 150d
//                ),
//                0.01
//        );

        heightNoiseGradient = new NoiseGradientTransformer(heightNoise, 0.5d);



        paletteNoise = new WhiteNoiseGenerator(seed + 2);
        featureNoise = new WhiteNoiseGenerator(seed + 6);
    }

    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn col) {
        if (col.chunkY >= 0 && col.chunkY <= 15) {
            int maxCy = Math.floorDiv(maxHeight, 16);

            var columnDescriptor = new EarthZoneColumnDescriptor(col);
            columnDescriptor.buildCache2d("height", heightNoise);
            columnDescriptor.buildCache2d("gradient", heightNoiseGradient);
            columnDescriptor.buildCache2d("palette", paletteNoise);

            for(int cy = col.chunkY; cy <= maxCy; cy++) {
                Chunk chunk = zone.getChunkAtChunkCoords(col.chunkX, cy, col.chunkZ);
                if (chunk == null) {
                    chunk = new Chunk(col.chunkX, cy, col.chunkZ);
                    chunk.initChunkData(chunkDataFactory);

                    col.addChunk(chunk);
                    zone.addChunk(chunk);
                }

                generateChunk(chunk, columnDescriptor);
            }

            generateFeatures(zone, col, columnDescriptor);
        }
    }

    private void generateChunk(Chunk chunk, EarthZoneColumnDescriptor columnDescriptor) {
        int globalX = chunk.blockX;
        int globalY = chunk.blockY;
        int globalZ = chunk.blockZ;

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double height = columnDescriptor.readCache2d("height", localX, localZ);
                double gradient = columnDescriptor.readCache2d("gradient", localX, localZ);
                double paletteOffset = columnDescriptor.readCache2d("palette", localX, localZ);

                for (int localY = 0; localY < CHUNK_WIDTH; localY++, globalY++) {
                    if(globalY > height) {
                        if(globalY <= waterHeight) {
                            chunk.setBlockState(waterBlock, localX, localY, localZ);
                        }
                        continue;
                    }

//                    Grass/top layer
                    if(globalY > height - 1) {
                        if(gradient < 0.9) {
                            if(height <= shoreHeight) {
                                chunk.setBlockState(gradient < 0.7 ? sandBlock : gravelBlock, localX, localY, localZ);
                            } else {
                                chunk.setBlockState(gradient < 0.4 ? fullGrassBlock : grassBlock, localX, localY, localZ);
                            }
                            continue;
                        } else if(height > shoreHeight) {
                            var paletteBlock = gravelBlock;

                            if(paletteOffset > 0.0) {
                                paletteBlock = stoneBlock;
                            } else if(paletteOffset > -0.5) {
                                paletteBlock = grassBlock;
                            }

                            chunk.setBlockState(paletteBlock, localX, localY, localZ);
                            continue;
                        } else {
                            chunk.setBlockState(stoneBlock, localX, localY, localZ);
                            continue;
                        }
                    }

                    // Dirt layer
                    if(globalY > height - 5) {
                        if(gradient < 1.0) {
                            if(height <= shoreHeight) {
                                chunk.setBlockState(gradient < 0.9 ? sandBlock : gravelBlock, localX, localY, localZ);
                            } else {
                                chunk.setBlockState(dirtBlock, localX, localY, localZ);
                            }
                        } else {
                            chunk.setBlockState(stoneBlock, localX, localY, localZ);
                        }
                        continue;
                    }

                    chunk.setBlockState(stoneBlock, localX, localY, localZ);
                }
                globalY -= CHUNK_WIDTH;
            }
            globalZ -= CHUNK_WIDTH;
        }
    }

    private void generateFeatures(Zone zone, ChunkColumn column, EarthZoneColumnDescriptor columnDescriptor) {
        int globalX = column.getBlockX();
        int globalZ = column.getBlockZ();

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for (int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                var height = columnDescriptor.readCache2d("height", localX, localZ);
                var gradient = columnDescriptor.readCache2d("gradient", localX, localZ);
                var featureValue = featureNoise.sample(globalX, globalZ);

                var globalY = (int) Math.round(height + 0.5);
                var ground = zone.getBlockState(globalX, globalY - 1, globalZ);
                var air = zone.getBlockState(globalX, globalY, globalZ);

                // Tree generation
                if(
                        featureValue > 0.95 && (
                                ground.equals(grassBlock) ||
                                ground.equals(dirtBlock) ||
                                ground.equals(sandBlock)
                        ) &&
                        air.equals(airBlock)
                ) {
//                    CosmicEarthMod.LOGGER.info("place tree on gradient " + gradient);
                    // Coconut trees
                    if(
                            height < waterHeight + 24d &&
                            height > 48d &&
                            gradient < 0.2d
                    ) {
//                        CosmicEarthMod.LOGGER.info("\\- coconut tree");
                        CoconutTree.generateTree(this.seed, zone, globalX, globalY, globalZ);
                        continue;
                    }
                    // Pine trees
                    else if(
                            height > shoreHeight + 10d &&
                            height < maxHeight - 48d &&
                            gradient < 0.6d
                    ) {
//                        CosmicEarthMod.LOGGER.info("\\- pine tree");
                        pineTreeStructure.generate(this.seed, zone, globalX, globalY, globalZ);
                        continue;
                    }
                    // Oak trees
                    else if(featureValue > 0.99) {
//                        CosmicEarthMod.LOGGER.info("\\- oak tree");
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
