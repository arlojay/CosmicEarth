package com.arlojay.cosmicearth.lib.noise;

import com.arlojay.cosmicearth.lib.noise.impl.CustomNoiseTransformer;

public abstract class SingleInputNoiseTransformer implements NoiseNode {
    protected final NoiseNode source;

    @Override
    public String buildString() {
        return NoiseDebugString.buildStringSubnode(source);
    }

    public SingleInputNoiseTransformer(NoiseNode source) {
        this.source = source;
    }

    @Override
    public double sample(double t) {
        return transform(source.sample(t));
    }

    @Override
    public double sample(double x, double y) {
        return transform(source.sample(x, y));
    }

    @Override
    public double sample(double x, double y, double z) {
        return transform(source.sample(x, y, z));
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return transform(source.sample(x, y, z, w));
    }

    protected abstract double transform(double sample);

    @Override
    public void setSeed(long seed) {
        source.setSeed(seed);
    }
}
