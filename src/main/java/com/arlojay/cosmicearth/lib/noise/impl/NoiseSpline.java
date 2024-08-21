package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.spline.SplineMapper;

public class NoiseSpline extends SingleInputNoiseTransformer {
    private final SplineMapper mapper;

    public NoiseSpline(NoiseNode source, SplineMapper mapper) {
        super(source);
        this.mapper = mapper;
    }

    @Override
    protected double transform(double sample) {
        return mapper.interpolate(sample);
    }
}
