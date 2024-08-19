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
    public int maxHeight = 255;
    BlockState airBlock = this.getBlockStateInstance("base:air[default]");
    BlockState stoneBlock = this.getBlockStateInstance("base:stone_basalt[default]");
    BlockState grassBlock = this.getBlockStateInstance("base:grass[default]");
    BlockState dirtBlock = this.getBlockStateInstance("base:dirt[default]");

    private final IBlockDataFactory<BlockState> chunkDataFactory = () -> new SingleBlockData<>(airBlock);

    private NoiseNode baseNoise;
    private NoiseNode heightNoise;
    private NoiseNode heightNoiseGradient;

    @Override
    public void create() {
        baseNoise = new OctaveNoise(new SimplexNoiseGenerator(seed), 4, 0.5, 2.0, 1.0);
        baseNoise = new NoiseScaler(new NoiseSpline(baseNoise, new NoiseSpline.SplinePoint[]{
                new NoiseSpline.SplinePoint(-1, -1),
                new NoiseSpline.SplinePoint(-0.5, -0.2),
                new NoiseSpline.SplinePoint(0, 0),
                new NoiseSpline.SplinePoint(0.5, 0.1),
                new NoiseSpline.SplinePoint(0.8, 0.3),
                new NoiseSpline.SplinePoint(1, 1)
        }), 0.01);
        heightNoise = new NoiseMapper(baseNoise, maxHeight / 2d - 32d, maxHeight / 2d + 32d);
        heightNoiseGradient = new NoiseGradientTransformer(baseNoise);
//        heightNoise = new NoiseScaler(new SimplexNoiseGenerator(seed), 0.01);
    }

    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn col) {
        if (col.chunkY >= 0 && col.chunkY <= 15) {
            int maxCy = Math.floorDiv(this.maxHeight, 16);

//            for(int cy = maxCy; cy >= col.chunkY; cy--) {
            for(int cy = col.chunkY; cy <= maxCy; cy++) {
                Chunk chunk = zone.getChunkAtChunkCoords(col.chunkX, cy, col.chunkZ);
                if (chunk == null) {
                    chunk = new Chunk(col.chunkX, cy, col.chunkZ);
                    chunk.initChunkData(chunkDataFactory);

                    col.addChunk(chunk);
                    zone.addChunk(chunk);
                }

                generateChunk(chunk);
            }
        }
    }

    private void generateChunk(Chunk chunk) {
        int globalX = chunk.blockX;
        int globalY = chunk.blockY;
        int globalZ = chunk.blockZ;

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                double height = heightNoise.sample(globalX, globalZ);
                double gradient = heightNoiseGradient.sample(globalX, globalZ);

                for (int localY = 0; localY < CHUNK_WIDTH; localY++, globalY++) {
                    if(globalY > height) {
                        chunk.setBlockState(airBlock, localX, localY, localZ);
                        continue;
                    }

                    if(globalY > height - 1 && gradient < 0.1) {
                        chunk.setBlockState(grassBlock, localX, localY, localZ);
                        continue;
                    }
                    if(globalY > height - 5 && gradient < 0.1) {
                        chunk.setBlockState(dirtBlock, localX, localY, localZ);
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
