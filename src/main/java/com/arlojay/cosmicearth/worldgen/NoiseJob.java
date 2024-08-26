package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class NoiseJob {
    private final double[] samples;
    private final NoiseNode noise;
    private final int blockOriginX;
    private final int blockOriginY;
    private final int blockOriginZ;
    private final Object lock;
    private boolean finished;

    public NoiseJob(NoiseNode noise, int blockOriginX, int blockOriginY, int blockOriginZ, double[] samples) {
        this.noise = noise;
        this.blockOriginX = blockOriginX;
        this.blockOriginY = blockOriginY;
        this.blockOriginZ = blockOriginZ;
        this.samples = samples;
        this.lock = new Object();
    }

    public void waitForFinish() {
        if(finished) return;

        try {
            synchronized (lock) { lock.wait(100); }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void fulfill() {
        NoiseChunkSampler.sample(noise, samples, blockOriginX, blockOriginY, blockOriginZ);
        finished = true;
        synchronized (lock) { lock.notify(); }
    }
}