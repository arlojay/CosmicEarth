package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import libs.opensimplex2.OpenSimplex2;

public class SimplexNoiseGenerator extends NoiseGenerator {
    public SimplexNoiseGenerator(long seed) {
        super(seed);
    }

    @Override
    public double sample(double t) {
        return OpenSimplex2.noise2(seed, t, t);
    }

    @Override
    public double sample(double x, double y) {
        return OpenSimplex2.noise2(seed, x, y);
    }

    @Override
    public double sample(double x, double y, double z) {
        return OpenSimplex2.noise3_ImproveXZ(seed, x, y, z);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return OpenSimplex2.noise4_ImproveXYZ(seed, x, y, z, w);
    }
}
