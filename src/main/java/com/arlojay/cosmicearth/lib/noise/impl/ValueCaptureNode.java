package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;

public class ValueCaptureNode implements NoiseNode {
    private final NoiseNode noise;
    private double lastValue = 0d;

    public ValueCaptureNode(NoiseNode noise) {
        this.noise = noise;
    }

    public ValueCaptureNode asCopy() {
        return new ValueCaptureNode(noise.asCopy());
    }

    public double getLastValue() {
        return lastValue;
    }

    @Override
    public double sample(double t) {
        return this.lastValue = noise.sample(t);
    }

    @Override
    public double sample(double x, double y) {
        return this.lastValue = noise.sample(x, y);
    }

    @Override
    public double sample(double x, double y, double z) {
        return this.lastValue = noise.sample(x, y, z);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return this.lastValue = noise.sample(x, y, z, w);
    }

    @Override
    public void setSeed(long seed) {
        this.noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@ValueCapture" + NoiseDebugString.createPropertyList(
                "lastValue", lastValue
        ) + NoiseDebugString.buildStringSubnode(this.noise);
    }
}
