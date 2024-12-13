package com.arlojay.cosmicearth.worldgen.noise;

import com.arlojay.cosmicearth.lib.memory.RecyclingPool;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static finalforeach.cosmicreach.world.Chunk.CHUNK_WIDTH;

public class NoiseCache2D implements NoiseCache {
    public static final RecyclingPool<NoiseCache2D> recyclingPool = new RecyclingPool<>(NoiseCache2D::new);

    private int blockX;
    private int blockZ;
    private final Map<String, double[]> caches2d;

    protected NoiseCache2D() {
        this.caches2d = new HashMap<>();
    }

    public static NoiseCache2D create(int blockX, int blockZ) {
        var old = recyclingPool.get();
        old.blockX = blockX;
        old.blockZ = blockZ;

        return old;
    }
    public void recycle() {
        recyclingPool.recycle(this);
    }

    public void build(String id, NoiseNode noise) {
        double[] cache = NoiseChunkSampler.sample(noise, getCache(id), blockX, blockZ);
        caches2d.put(id, cache);
    }

    public void clear(String id) {
        double[] cache = getCache(id);

        Arrays.fill(cache, 0);
    }

    public double read(String id, int x, int y) {
        return caches2d.get(id)[x * CHUNK_WIDTH + y];
    }

    public void write(String id, int x, int y, double value) {
        getCache(id)[x * CHUNK_WIDTH + y] = value;
    }

    public double[] getCache(String id) {
        if(caches2d.containsKey(id)) {
            return caches2d.get(id);
        } else {
            var samples = new double[CHUNK_WIDTH * CHUNK_WIDTH];
            caches2d.put(id, samples);
            return samples;
        }
    }

    public void copyCache(String id, String newId) {
        var oldCache = getCache(id);
        var newCache = getCache(newId);

        System.arraycopy(oldCache, 0, newCache, 0, oldCache.length);
    }
}
