package com.arlojay.cosmicearth.lib.noise;

public abstract class NoiseGenerator implements NoiseNode {
    public long seed;

    public NoiseGenerator(long seed) {
        this.seed = seed;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
