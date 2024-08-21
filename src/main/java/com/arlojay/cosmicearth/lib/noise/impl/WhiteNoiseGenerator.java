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
        t = t * 4.170467398080612d + 500.9973862371664d;
        return noise.noise1D((float) t - (long) t);
    }

    @Override
    public double sample(double x, double y) {
        x = x * 3.9379393289309927d + 322.4421547362026d;
        y = y * 8.673643056243296d + 425.0491277233486d;
        return noise.noise2D((float) x, (float) y);
    }

    @Override
    public double sample(double x, double y, double z) {
        x = x * 3.234437639641057d + 136.73857063895102d;
        y = y * 9.384560606635128d + 766.8385882476493d;
        z = z * 2.937194202878184d + 820.1833282187521d;
        return noise.noise3D((float) x, (float) y, (float) z);
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        x = x * 9.99939588117808d + 776.0418669041953d;
        y = y * 2.462956744230127d + 772.8410036641549d;
        z = z * 6.609786545054767d + 666.9555339713697d;
        w = w * 3.9379393289309927d + 842.4192566478537d;
        return noise.noise3D(
                (float) (x + noise.noise1D((float) (w + y + z))),
                (float) (y + noise.noise1D((float) (w + x + z))),
                (float) (z + noise.noise1D((float) (w + x + y)))
        );
    }
}
