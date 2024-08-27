package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class NoiseJob extends ThreadJob {
    private final double[] samples;
    private final NoiseNode noise;
    private final int blockOriginX;
    private final int blockOriginY;
    private final int blockOriginZ;

    public NoiseJob(NoiseNode noise, int blockOriginX, int blockOriginY, int blockOriginZ, double[] samples) {
        this.noise = noise;
        this.blockOriginX = blockOriginX;
        this.blockOriginY = blockOriginY;
        this.blockOriginZ = blockOriginZ;
        this.samples = samples;
    }

    public void fulfill() {
        NoiseChunkSampler.sample(noise, samples, blockOriginX, blockOriginY, blockOriginZ);
        super.fulfill();
    }
}