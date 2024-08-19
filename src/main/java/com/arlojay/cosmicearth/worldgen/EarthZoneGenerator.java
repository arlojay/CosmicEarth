package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.*;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.savelib.blockdata.SingleBlockData;
import finalforeach.cosmicreach.savelib.blocks.IBlockDataFactory;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;

public class EarthZoneGenerator extends ZoneGenerator {
    public static int maxHeight = 255;
    public static int waterHeight = 100;
    public static int shoreHeight = 105;

    BlockState airBlock = this.getBlockStateInstance("base:air[default]");
    BlockState stoneBlock = this.getBlockStateInstance("base:stone_basalt[default]");
    BlockState grassBlock = this.getBlockStateInstance("base:grass[default]");
    BlockState dirtBlock = this.getBlockStateInstance("base:dirt[default]");
    BlockState sandBlock = this.getBlockStateInstance("base:sand[default]");
    BlockState waterBlock = this.getBlockStateInstance("base:water[default]");
    BlockState gravelBlock = this.getBlockStateInstance("base:stone_gravel[default]");

    private final IBlockDataFactory<BlockState> chunkDataFactory = () -> new SingleBlockData<>(airBlock);

    private NoiseNode heightNoise;
    private NoiseNode heightNoiseGradient;
    private NoiseNode voronoiBlobNoise;
    private NoiseNode paletteNoise;

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
                        new NoiseSpline.SplinePoint[]{
                            new NoiseSpline.SplinePoint(-1, -1),
                            new NoiseSpline.SplinePoint(-0.5, -0.4),
                            new NoiseSpline.SplinePoint(0, 0),
                            new NoiseSpline.SplinePoint(0.5, 0.2),
                            new NoiseSpline.SplinePoint(0.8, 0.6),
                            new NoiseSpline.SplinePoint(1, 1)
                        }
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
                        new NoiseSpline.SplinePoint[]{
                                new NoiseSpline.SplinePoint(   -1d,     0d  ),
                                new NoiseSpline.SplinePoint(   -2/3d,   1d  ),
                                new NoiseSpline.SplinePoint(    0d,    -1d  ),
                                new NoiseSpline.SplinePoint(    2/3d,   1d  ),
                                new NoiseSpline.SplinePoint(    1d,     0d  ),
                        }
                ),
                -40d, 100d
        );

        heightNoise = new NoiseMixer(new NoiseMixer.MixerSource[]{
                new NoiseMixer.MixerSource(
                        new NoiseScaler(shapedNoise, 0.001d),
                        1d
                ),
                new NoiseMixer.MixerSource(
                        new NoiseScaler(ridgesNoise, 0.0003d),
                        1d
                )
        }, false);

        heightNoise = new NoiseScaler(
                new NoiseMapper(
                        new ErodedNoise(new SimplexNoiseGenerator(seed + 4), 5, 2.0, 0.5),
                        70d, 150d
                ),
                0.001
        );

        heightNoiseGradient = new NoiseGradientTransformer(heightNoise, 0.0001d);
        voronoiBlobNoise = new NoiseScaler(
                new VoronoiGenerator(seed + 5, VoronoiGenerator.VoronoiMode.DISTANCE),
                0.02
        );



        paletteNoise = new WhiteNoiseGenerator(seed + 2);
    }

    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn col) {
        if (col.chunkY >= 0 && col.chunkY <= 15) {
            int maxCy = Math.floorDiv(this.maxHeight, 16);

            var columnDescriptor = new EarthZoneColumnDescriptor(col);
            columnDescriptor.buildCache2d("height", heightNoise);
            columnDescriptor.buildCache2d("gradient", heightNoiseGradient);
            columnDescriptor.buildCache2d("palette", paletteNoise);
//            columnDescriptor.buildCache2d("doubleGradient", heightNoiseDoubleGradient);

//            for(int cy = maxCy; cy >= col.chunkY; cy--) {
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
                        chunk.setBlockState(globalY <= waterHeight ? waterBlock : airBlock, localX, localY, localZ);
                        continue;
                    }

                    if(globalY > height - 1 && gradient < 1.0) {

                        if(gradient < 0.5) {
                            if(height <= shoreHeight) {
                                chunk.setBlockState(gradient < 0.2 ? sandBlock : gravelBlock, localX, localY, localZ);
                            } else {
                                chunk.setBlockState(grassBlock, localX, localY, localZ);
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
                        }
                    }

                    if(globalY > height - 5 && gradient < 0.4) {
                        if(height <= shoreHeight) {
                            chunk.setBlockState(gradient < 0.2 ? sandBlock : gravelBlock, localX, localY, localZ);
                        } else {
                            chunk.setBlockState(dirtBlock, localX, localY, localZ);
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
