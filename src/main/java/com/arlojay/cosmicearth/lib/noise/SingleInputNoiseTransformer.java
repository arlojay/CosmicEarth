package com.arlojay.cosmicearth.lib.noise;

public abstract class SingleInputNoiseTransformer implements NoiseNode {
    private final NoiseNode source;

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
}
