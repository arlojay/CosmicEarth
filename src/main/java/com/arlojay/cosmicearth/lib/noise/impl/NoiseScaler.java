package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseScaler implements NoiseNode {
    private final NoiseNode noise;
    private final double scalar;

    public static void register() {
        NoiseLoader.registerNoiseNode("scale", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("scale transformer must have a `source`");

            JsonObject sourceNode = sourceObject.asObject();
            double scalar = options.getDouble("scalar", 1d);

            return new NoiseScaler(NoiseLoader.createNoiseNode(sourceNode), scalar);
        });
    }

    public NoiseScaler(NoiseNode noise, double scalar) {
        this.noise = noise;
        this.scalar = scalar;
    }

    @Override
    public double sample(double t) {
        return noise.sample(t * scalar);
    }

    @Override
    public double sample(double x, double y) {
        return noise.sample(x * scalar, y * scalar);
    }

    @Override
    public double sample(double x, double y, double z) {
        return noise.sample(x * scalar, y * scalar, z * scalar);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return noise.sample(x * scalar, y * scalar, z * scalar, w * scalar);
    }

    @Override
    public void setSeed(long seed) {
        noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@NoiseScaler" + NoiseDebugString.createPropertyList(
                "scalar", scalar
        ) + NoiseDebugString.buildStringSubnode(noise);
    }
}
