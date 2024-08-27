package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class ConstantValueGenerator implements NoiseNode {
    private final double value;

    public static void register() {
        NoiseLoader.registerNoiseNode("const",
                (JsonObject options) -> new ConstantValueGenerator(
                        NoiseLoader.getProps().getDouble("value", 0d)
                )
        );
    }

    public ConstantValueGenerator(double value) {
        this.value = value;
    }

    @Override
    public ConstantValueGenerator asCopy() {
        return new ConstantValueGenerator(value);
    }


    @Override
    public double sample(double t) {
        return value;
    }

    @Override
    public double sample(double x, double y) {
        return value;
    }

    @Override
    public double sample(double x, double y, double z) {
        return value;
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return value;
    }

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public String buildString() {
        return "@Constant" + NoiseDebugString.createPropertyList(
                "value", value
        );
    }
}
