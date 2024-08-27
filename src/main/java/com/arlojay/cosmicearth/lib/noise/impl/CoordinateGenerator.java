package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class CoordinateGenerator extends NoiseGenerator {
    private final boolean doX;
    private final boolean doY;
    private final boolean doZ;
    private final boolean doW;

    public static void register() {
        NoiseLoader.registerNoiseNode("coordinate", (JsonObject options) -> {
            boolean doX = options.getBoolean("x", false);
            boolean doY = options.getBoolean("y", false);
            boolean doZ = options.getBoolean("z", false);
            boolean doW = options.getBoolean("w", false);

            return new CoordinateGenerator(doX, doY, doZ, doW);
        });
    }

    public CoordinateGenerator(boolean doX, boolean doY, boolean doZ, boolean doW) {
        super(0L, 0L);

        this.doX = doX;
        this.doY = doY;
        this.doZ = doZ;
        this.doW = doW;
    }

    public CoordinateGenerator asCopy() {
        return new CoordinateGenerator(doX, doY, doZ, doW);
    }

    @Override
    public double sample(double t) {
        return doX ? t : 0;
    }

    @Override
    public double sample(double x, double y) {
        return (doX ? x : 0) + (doY ? y : 0);
    }

    @Override
    public double sample(double x, double y, double z) {
        return (doX ? x : 0) + (doY ? y : 0) + (doZ ? z : 0);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return (doX ? x : 0) + (doY ? y : 0) + (doZ ? z : 0) + (doW ? w : 0);
    }

    @Override
    public String buildString() {
        return "@CoordinateGenerator" + NoiseDebugString.createPropertyList(
                "doX", doX,
                "doY", doY,
                "doZ", doZ,
                "doW", doW
        );
    }
}
