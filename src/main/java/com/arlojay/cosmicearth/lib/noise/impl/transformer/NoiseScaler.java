package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseScaler implements NoiseNode {
    private final NoiseNode noise;
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;
    private final double scaleW;

    public static void register() {
        NoiseLoader.registerNoiseNode("scale", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("scale transformer must have a `source`");

            double scalar = options.getDouble("scalar", 1d);
            double scaleX = options.getDouble("scale_x", scalar);
            double scaleY = options.getDouble("scale_y", scalar);
            double scaleZ = options.getDouble("scale_z", scalar);
            double scaleW = options.getDouble("scale_w", scalar);

            return new NoiseScaler(NoiseLoader.createNoiseNode(sourceObject), scaleX, scaleY, scaleZ, scaleW);
        });
    }

    public NoiseScaler(NoiseNode noise, double scalar) {
        this(noise, scalar, scalar, scalar, scalar);
    }
    public NoiseScaler(NoiseNode noise, double scaleX, double scaleY, double scaleZ, double scaleW) {
        this.noise = noise;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.scaleW = scaleW;
    }

    @Override
    public NoiseScaler asCopy() {
        return new NoiseScaler(noise.asCopy(), scaleX, scaleY, scaleZ, scaleW);
    }

    @Override
    public double sample(double t) {
        return noise.sample(t / scaleX);
    }

    @Override
    public double sample(double x, double y) {
        return noise.sample(x / scaleX, y / scaleY);
    }

    @Override
    public double sample(double x, double y, double z) {
        return noise.sample(x / scaleX, y / scaleY, z / scaleZ);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return noise.sample(x / scaleX, y / scaleY, z / scaleZ, w / scaleW);
    }

    @Override
    public void setSeed(long seed) {
        noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@NoiseScaler" + NoiseDebugString.createPropertyList(
                "scaleX", scaleX,
                "scaleY", scaleY,
                "scaleZ", scaleZ,
                "scaleW", scaleW
        ) + NoiseDebugString.buildStringSubnode(noise);
    }
}
