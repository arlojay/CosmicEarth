package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.worldgen.ChunkColumn;

import java.util.HashMap;
import java.util.Map;

import static finalforeach.cosmicreach.world.Chunk.CHUNK_WIDTH;

public class EarthZoneColumnDescriptor {
    private final ChunkColumn column;
    private final Map<String, double[]> caches2d;
    private final Map<String, double[]> caches3d;

    public EarthZoneColumnDescriptor(ChunkColumn column) {
        this.column = column;
        caches2d = new HashMap<>();
        caches3d = new HashMap<>();
    }

    public void buildCache2d(String id, NoiseNode noise) {
        int globalX = column.getBlockX();
        int globalZ = column.getBlockZ();

        double[] cache = new double[CHUNK_WIDTH * CHUNK_WIDTH];

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                cache[localX * CHUNK_WIDTH + localZ] = noise.sample(globalX, globalZ);
            }
            globalZ -= CHUNK_WIDTH;
        }

        caches2d.put(id, cache);
    }

    public void buildCache3d(String id, NoiseNode noise) {
        int globalX = column.getBlockX();
        int globalY = column.chunkY * CHUNK_WIDTH;
        int globalZ = column.getBlockZ();

        double[] cache = new double[CHUNK_WIDTH * CHUNK_WIDTH * Region.TOTAL_BLOCK_HEIGHT];

        for(int localX = 0; localX < CHUNK_WIDTH; localX++, globalX++) {
            for(int localZ = 0; localZ < CHUNK_WIDTH; localZ++, globalZ++) {
                for(int localY = 0; localY < Region.TOTAL_BLOCK_HEIGHT; localY++, globalY++) {
                    cache[localY * CHUNK_WIDTH * CHUNK_WIDTH + localX * CHUNK_WIDTH + localZ] = noise.sample(globalX, globalY, globalZ);
                }
                globalY -= Region.TOTAL_BLOCK_HEIGHT;
            }
            globalZ -= CHUNK_WIDTH;
        }

        caches3d.put(id, cache);
    }


    public double readCache2d(String id, int x, int z) {
        return caches2d.get(id)[x * CHUNK_WIDTH + z];
    }

    public double readCache3d(String id, int x, int y, int z) {
        return caches2d.get(id)[y * CHUNK_WIDTH * CHUNK_WIDTH + x * CHUNK_WIDTH + z];
    }
}
