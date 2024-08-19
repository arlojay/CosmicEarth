package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.MultiInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class NoiseAdder extends MultiInputNoiseTransformer {
    public NoiseAdder(NoiseNode[] sources) {
        super(sources);
    }

    @Override
    protected double transform(double[] samples) {
        double total = 0d;
        for (double sample : samples) total += sample;

        return total / (double) sourceCount;
    }
}
