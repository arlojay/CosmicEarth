package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

public class WhiteNoiseGenerator extends NoiseGenerator {
    private final WhiteNoise noise;

    public WhiteNoiseGenerator(long seed) {
        super(seed);

        this.noise = new WhiteNoise(seed);
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
}
