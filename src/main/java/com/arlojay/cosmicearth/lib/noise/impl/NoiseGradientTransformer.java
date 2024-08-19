package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class NoiseGradientTransformer implements NoiseNode {
    private final NoiseNode noise;
    private final double h;

    public NoiseGradientTransformer(NoiseNode noise) {
        this(noise, 0.001);
    }
    public NoiseGradientTransformer(NoiseNode noise, double h) {
        this.noise = noise;
        this.h = h;
    }

    @Override
    public double sample(double t) {
        var deltaT = (noise.sample(t + h) - noise.sample(t - h)) / (h * 2d);
        return Math.abs(deltaT);
    }

    @Override
    public double sample(double x, double y) {
        var deltaX = (noise.sample(x + h, y) - noise.sample(x - h, y)) / (h * 2d);
        var deltaY = (noise.sample(x, y + h) - noise.sample(x, y - h)) / (h * 2d);

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public double sample(double x, double y, double z) {
        var deltaX = (noise.sample(x + h, y, z) - noise.sample(x - h, y, z)) / (h * 2d);
        var deltaY = (noise.sample(x, y + h, z) - noise.sample(x, y - h, z)) / (h * 2d);
        var deltaZ = (noise.sample(x, y, z + h) - noise.sample(x, y, z - h)) / (h * 2d);

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        var deltaX = (noise.sample(x + h, y, z, w) - noise.sample(x - h, y, z, w)) / (h * 2d);
        var deltaY = (noise.sample(x, y + h, z, w) - noise.sample(x, y - h, z, w)) / (h * 2d);
        var deltaZ = (noise.sample(x, y, z + h, w) - noise.sample(x, y, z - h, w)) / (h * 2d);
        var deltaW = (noise.sample(x, y, z, w + h) - noise.sample(x, y, z, w - h)) / (h * 2d);

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ + deltaW * deltaW);
    }
}
