package com.arlojay.cosmicearth.lib.noise;

public abstract class NoiseGenerator implements NoiseNode {
    public final long seed;

    public NoiseGenerator(long seed) {
        this.seed = seed;
    }
}
