package com.arlojay.cosmicearth.worldgen.noise;

import com.arlojay.cosmicearth.lib.memory.RecyclingPool;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static finalforeach.cosmicreach.world.Chunk.CHUNK_WIDTH;

public class NoiseCache3D implements NoiseCache {
    public static final RecyclingPool<NoiseCache3D> recyclingPool = new RecyclingPool<>(NoiseCache3D::new);

    private int blockX;
    private int blockY;
    private int blockZ;
    private final Map<String, double[]> caches3d;

    protected NoiseCache3D() {
        this.caches3d = new HashMap<>();
    }

    public static NoiseCache3D create(int blockX, int blockY, int blockZ) {
        var old = recyclingPool.get();
        old.blockX = blockX;
        old.blockY = blockY;
        old.blockZ = blockZ;

        return old;
    }
    public void recycle() {
        recyclingPool.recycle(this);
    }

    public void build(String id, NoiseNode noise) {
        double[] cache = NoiseChunkSampler.sample(noise, getCache(id), blockX, blockY, blockZ);
        caches3d.put(id, cache);
    }

    public void clear(String id) {
        double[] cache = getCache(id);

        Arrays.fill(cache, 0);
        caches3d.put(id, cache);
    }

    public double read(String id, int x, int y, int z) {
        return caches3d.get(id)[y * CHUNK_WIDTH * CHUNK_WIDTH + x * CHUNK_WIDTH + z];
    }

    public void write(String id, int x, int y, int z, double value) {
        getCache(id)[y * CHUNK_WIDTH * CHUNK_WIDTH + x * CHUNK_WIDTH + z] = value;
    }

    public double[] getCache(String id) {
        if(caches3d.containsKey(id)) {
            return caches3d.get(id);
        } else {
            var samples = new double[CHUNK_WIDTH * CHUNK_WIDTH * CHUNK_WIDTH];
            caches3d.put(id, samples);
            return samples;
        }
    }
    public void copyCache(String id, String newId) {
        var oldCache = getCache(id);
        var newCache = getCache(newId);

        System.arraycopy(oldCache, 0, newCache, 0, oldCache.length);
    }
}
