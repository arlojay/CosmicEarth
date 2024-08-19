package com.arlojay.cosmicearth.lib.noise;

public class OctaveNoiseSampler {
    public static double sample1D(NoiseNode noise, double t, int detail, double roughness, double lacunarity, double distortion) {
        if(distortion != 0.0d) {
            t += noise.sample(t + 79.70650251998678d) * distortion;
        }

        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(int i = 0; i < detail; i++) {
            max += effectiveness;
            value += noise.sample(t * scale) * effectiveness;

            effectiveness *= roughness;
            scale *= lacunarity;
        }
        return value / max;
    }
    public static double sample2D(NoiseNode noise, double x, double y, int detail, double roughness, double lacunarity, double distortion) {
        if(distortion != 0.0d) {
            x += noise.sample(x + 187.3475077705784d, y + 304.7182892045774) * distortion;
            y += noise.sample(x + 574.7741054326257d, y + 727.5476841481425) * distortion;
        }

        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(int i = 0; i < detail; i++) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale) * effectiveness;

            effectiveness *= roughness;
            scale *= lacunarity;
        }
        return value / max;
    }
    public static double sample3D(NoiseNode noise, double x, double y, double z, int detail, double roughness, double lacunarity, double distortion) {
        if(distortion != 0.0d) {
            x += noise.sample(x + 720.7530757031899d, y + 630.0809779460403, z + 562.8202719963649) * distortion;
            y += noise.sample(x + 807.2943561962902d, y + 956.9505378437133, z + 469.0741515073502) * distortion;
            z += noise.sample(x + 190.9268231977717d, y + 773.6563178336033, z + 620.4209881004765) * distortion;
        }

        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(int i = 0; i < detail; i++) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale, z * scale) * effectiveness;

            effectiveness *= roughness;
            scale *= lacunarity;
        }
        return value / max;
    }
    public static double sample4D(NoiseNode noise, double x, double y, double z, double w, int detail, double roughness, double lacunarity, double distortion) {
        if(distortion != 0.0d) {
            x += noise.sample(x + 840.5710651524474d, y + 279.9952781849746, z + 459.5819482923276, w + 470.0537129999991) * distortion;
            y += noise.sample(x + 284.7644906307658d, y + 578.7510039941111, z + 715.3533319641978, w + 489.6809640118021) * distortion;
            z += noise.sample(x + 687.2412190545887d, y + 840.1166221979693, z + 574.3024728786922, w + 487.7604928342420) * distortion;
            w += noise.sample(x + 262.2764875080167d, y + 423.4805327468889, z + 738.4261606156668, w + 295.5659973309972) * distortion;
        }

        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(int i = 0; i < detail; i++) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale, z * scale, w * scale) * effectiveness;

            effectiveness *= roughness;
            scale *= lacunarity;
        }
        return value / max;
    }
}
