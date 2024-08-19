package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class NoiseScaler implements NoiseNode {
    private final NoiseNode noise;
    private final double scale;

    public NoiseScaler(NoiseNode noise, double scale) {
        this.noise = noise;
        this.scale = scale;
    }

    @Override
    public double sample(double t) {
        return noise.sample(t * scale);
    }

    @Override
    public double sample(double x, double y) {
        return noise.sample(x * scale, y * scale);
    }

    @Override
    public double sample(double x, double y, double z) {
        return noise.sample(x * scale, y * scale, z * scale);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return noise.sample(x * scale, y * scale, z * scale, w * scale);
    }
}
