package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.OctaveNoiseSampler;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class ErodedNoise implements NoiseNode {
    private final NoiseNode noise;
    private final int detail;
    private final int depth;
    private final double lacunarity;
    private final double roughness;
    private final double distortion;
    private final double stride;

    public static void register() {
        NoiseLoader.registerNoiseNode("erosion", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("erosion transformer must have a `source`");

            int detail = options.getInt("detail", 2);
            int depth = options.getInt("depth", 2);
            double lacunarity = options.getDouble("lacunarity", 2d);
            double stride = options.getDouble("stride", 2d);
            double roughness = options.getDouble("roughness", 0.5d);
            double distortion = options.getDouble("distortion", 0d);


            return new ErodedNoise(
                    NoiseLoader.createNoiseNode(sourceObject),
                    detail, depth,
                    lacunarity, stride,
                    roughness, distortion
            );
        });
    }

    public ErodedNoise(NoiseNode noise, int detail, double lacunarity, double roughness) {
        this(noise, detail, 2, lacunarity, 2.0, roughness, 0.0);
    }

    public ErodedNoise(NoiseNode noise, int detail, int depth, double lacunarity, double stride, double roughness, double distortion) {
        this.noise = noise;
        this.detail = detail;
        this.depth = depth;
        this.lacunarity = lacunarity;
        this.stride = stride;
        this.roughness = roughness;
        this.distortion = distortion;
    }

    public ErodedNoise asCopy() {
        return new ErodedNoise(noise.asCopy(), detail, depth, lacunarity, stride, roughness, distortion);
    }

    @Override
    public double sample(double t) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        double lacunarity = this.lacunarity;

        for(int i = 0; i < depth; i++) {
            var sample = Math.pow(Math.abs(OctaveNoiseSampler.sample1D(
                    noise, t * scale,
                    detail, roughness, lacunarity, distortion
            )) * 2.0d - 1.0d, 0.5d);

            max += effectiveness;
            value += sample * effectiveness;
            scale *= stride;

            effectiveness *= roughness;
            lacunarity *= 0.75d * sample + 1.25d;
        }

        return value / max;
    }

    @Override
    public double sample(double x, double y) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        double lacunarity = this.lacunarity;

        for(int i = 0; i < depth; i++) {
            var sample = Math.abs(OctaveNoiseSampler.sample2D(
                    noise, x * scale, y * scale,
                    detail, roughness, lacunarity, distortion
            )) * 2.0d - 1.0d;

            max += effectiveness;
            value += sample * effectiveness;
            scale *= stride;

            effectiveness *= roughness;
            lacunarity *= 0.75d * sample + 1.25d;
        }

        return value / max;
    }

    @Override
    public double sample(double x, double y, double z) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        double lacunarity = this.lacunarity;

        for(int i = 0; i < depth; i++) {
            var sample = Math.abs(OctaveNoiseSampler.sample3D(
                    noise, x * scale, y * scale, z * scale,
                    detail, roughness, lacunarity, distortion
            )) * 2.0d - 1.0d;

            max += effectiveness;
            value += sample * effectiveness;
            scale *= stride;

            effectiveness *= roughness;
            lacunarity *= 0.75d * sample + 1.25d;
        }

        return value / max;
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        double lacunarity = this.lacunarity;

        for(int i = 0; i < depth; i++) {
            var sample = Math.abs(OctaveNoiseSampler.sample4D(
                    noise, x * scale, y * scale, z * scale, w * scale,
                    detail, roughness, lacunarity, distortion
            )) * 2.0d - 1.0d;

            max += effectiveness;
            value += sample * effectiveness;
            scale *= stride;

            effectiveness *= roughness;
            lacunarity *= 0.75d * sample + 1.25d;
        }

        return value / max;
    }

    @Override
    public void setSeed(long seed) {
        noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@ErodedNoise" + NoiseDebugString.createPropertyList(
                "detail", this.detail,
                "depth", this.depth,
                "lacunarity", this.lacunarity,
                "stride", this.stride,
                "roughness", this.roughness,
                "distortion", this.distortion
        ) + NoiseDebugString.buildStringSubnode(noise);
    }
}
