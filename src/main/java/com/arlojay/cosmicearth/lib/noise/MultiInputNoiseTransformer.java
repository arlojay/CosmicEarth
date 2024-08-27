package com.arlojay.cosmicearth.lib.noise;

public abstract class MultiInputNoiseTransformer implements NoiseNode {
    protected final NoiseNode[] sources;
    protected final double[] samples;
    protected final int sourceCount;

    public MultiInputNoiseTransformer(NoiseNode[] sources) {
        this.sources = sources;
        this.sourceCount = sources.length;
        this.samples = new double[sourceCount];
    }

    @Override
    public String buildString() {
        return NoiseDebugString.buildStringSubnode(sources);
    }

    @Override
    public double sample(double t) {
        for(int i = 0; i < sources.length; i++) samples[i] = sources[i].sample(t);
        return transform(samples);
    }

    @Override
    public double sample(double x, double y) {
        for(int i = 0; i < sources.length; i++) samples[i] = sources[i].sample(x, y);
        return transform(samples);
    }

    @Override
    public double sample(double x, double y, double z) {
        for(int i = 0; i < sources.length; i++) samples[i] = sources[i].sample(x, y, z);
        return transform(samples);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        for(int i = 0; i < sources.length; i++) samples[i] = sources[i].sample(x, y, z, w);
        return transform(samples);
    }

    protected abstract double transform(double[] samples);


    @Override
    public void setSeed(long seed) {
        for(var source : sources) source.setSeed(seed);
    }
}
