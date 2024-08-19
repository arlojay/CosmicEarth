package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;

public class NoiseMapper extends SingleInputNoiseTransformer {
    private final double outputMin;
    private final double outputMax;
    private final double inputMin;
    private final double inputMax;

    public NoiseMapper(NoiseNode source, double min, double max) {
        this(source, -1d, 1d, min, max);
    }

    public NoiseMapper(NoiseNode source, double inputMin, double inputMax, double outputMin, double outputMax) {
        super(source);
        this.inputMin = inputMin;
        this.inputMax = inputMax;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
    }

    @Override
    protected double transform(double sample) {
        return (sample - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }
}
