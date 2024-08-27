package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;
import org.hjson.JsonObject;

public class WhiteNoiseGenerator extends NoiseGenerator {
    private final WhiteNoise noise;

    public static void register() {
        NoiseLoader.registerNoiseNode("noise",
                (JsonObject options) -> new SimplexNoiseGenerator(
                        NoiseLoader.getProps().getLong("seed", 0L),
                        options.getLong("seed", 0L)
                )
        );
    }

    public WhiteNoiseGenerator(long seed) {
        this(seed, 0L);
    }
    public WhiteNoiseGenerator(long seed, long seedOffset) {
        super(seed, seedOffset);

        this.noise = new WhiteNoise(seed);
    }

    public WhiteNoiseGenerator asCopy() {
        return new WhiteNoiseGenerator(seed, seedOffset);
    }

    @Override
    public double sample(double t) {
        return noise.noise1D((float) t);
    }

    @Override
    public double sample(double x, double y) {
        return noise.noise2D((float) x, (float) y);
    }

    @Override
    public double sample(double x, double y, double z) {
        return noise.noise3D((float) x, (float) y, (float) z);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return noise.noise3D(
                (float) (x + noise.noise1D((float) (w + y + z))),
                (float) (y + noise.noise1D((float) (w + x + z))),
                (float) (z + noise.noise1D((float) (w + x + y)))
        );
    }

    @Override
    public String buildString() {
        return "@WhiteNoise" + NoiseDebugString.createPropertyList(
                "seed", seed,
                "seedOffset", seedOffset
        );
    }
}
