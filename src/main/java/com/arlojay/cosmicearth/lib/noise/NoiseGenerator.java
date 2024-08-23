package com.arlojay.cosmicearth.lib.noise;

public abstract class NoiseGenerator implements NoiseNode {
    protected long seed;
    protected final long seedOffset;

    public NoiseGenerator(long seed, long seedOffset) {
        this.seed = seed;
        this.seedOffset = seedOffset;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed + seedOffset;
    }
}
