package com.arlojay.cosmicearth.worldgen;

import finalforeach.cosmicreach.world.Chunk;

import java.util.Arrays;

public class ChunkMask {
    public static final RecyclingPool<ChunkMask> recyclingPool = new RecyclingPool<>(new RecyclingFactory<>() {
        @Override
        public ChunkMask createNew() {
            return new ChunkMask();
        }

        @Override
        public void recycle(ChunkMask old) {
            Arrays.fill(old.mask, false);
        }
    });

    public static ChunkMask create() {
        return recyclingPool.get();
    }
    public void recycle() {
        recyclingPool.recycle(this);
    }

    public boolean[] mask = new boolean[Chunk.NUM_BLOCKS_IN_CHUNK];

    public boolean get(int x, int y, int z) {
        return mask[x * Chunk.CHUNK_WIDTH * Chunk.CHUNK_WIDTH + y * Chunk.CHUNK_WIDTH + z];
    }

    public void set(int x, int y, int z, boolean enabled) {
        if(x >= Chunk.CHUNK_WIDTH || x < 0 || y >= Chunk.CHUNK_WIDTH || y < 0 || z >= Chunk.CHUNK_WIDTH || z < 0) return;

        mask[x * Chunk.CHUNK_WIDTH * Chunk.CHUNK_WIDTH + y * Chunk.CHUNK_WIDTH + z] = enabled;
    }
}
