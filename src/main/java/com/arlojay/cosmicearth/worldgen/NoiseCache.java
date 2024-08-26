package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static finalforeach.cosmicreach.world.Chunk.CHUNK_WIDTH;

public class NoiseCache {
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final Map<String, double[]> caches2d;
    private final Map<String, double[]> caches3d;

    public NoiseCache(int blockX, int blockY, int blockZ, NoiseCache oldDescriptor) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        caches2d = oldDescriptor == null ? new HashMap<>() : oldDescriptor.caches2d;
        caches3d = oldDescriptor == null ? new HashMap<>() : oldDescriptor.caches3d;
    }

    public void buildCache2d(String id, NoiseNode noise) {
        double[] cache = NoiseChunkSampler.sample(noise, getCache2d(id), blockX, blockZ);
        caches2d.put(id, cache);
    }

    public void buildCache3d(String id, NoiseNode noise) {
        double[] cache = NoiseChunkSampler.sample(noise, getCache3d(id), blockX, blockY, blockZ);
        caches3d.put(id, cache);
    }

    public void clearCache2d(String id) {
        double[] cache = getCache2d(id);

        Arrays.fill(cache, 0);
        caches3d.put(id, cache);
    }

    public void clearCache3d(String id) {
        double[] cache = getCache3d(id);

        Arrays.fill(cache, 0);
        caches3d.put(id, cache);
    }

    public double[] getCache2d(String id) {
        if(caches2d.containsKey(id)) {
            return caches2d.get(id);
        } else {
            var samples = new double[CHUNK_WIDTH * CHUNK_WIDTH];
            caches2d.put(id, samples);
            return samples;
        }
    }

    public double[] getCache3d(String id) {
        if(caches3d.containsKey(id)) {
            return caches3d.get(id);
        } else {
            var samples = new double[CHUNK_WIDTH * CHUNK_WIDTH * CHUNK_WIDTH];
            caches3d.put(id, samples);
            return samples;
        }
    }

    public double readCache2d(String id, int x, int y) {
        return caches2d.get(id)[x * CHUNK_WIDTH + y];
    }

    public double readCache3d(String id, int x, int y, int z) {
        return caches3d.get(id)[y * CHUNK_WIDTH * CHUNK_WIDTH + x * CHUNK_WIDTH + z];
    }

    public void writeCache2d(String id, int x, int y, double value) {
        getCache2d(id)[x * CHUNK_WIDTH + y] = value;
    }
    public void writeCache3d(String id, int x, int y, int z, double value) {
        getCache3d(id)[y * CHUNK_WIDTH * CHUNK_WIDTH + x * CHUNK_WIDTH + z] = value;
    }

    public void cloneCache2d(String id, String newId) {
        var oldCache = getCache2d(id);
        var newCache = getCache2d(newId);

        System.arraycopy(oldCache, 0, newCache, 0, oldCache.length);
    }
    public void cloneCache3d(String id, String newId) {
        var oldCache = getCache3d(id);
        var newCache = getCache3d(newId);

        System.arraycopy(oldCache, 0, newCache, 0, oldCache.length);
    }
}
