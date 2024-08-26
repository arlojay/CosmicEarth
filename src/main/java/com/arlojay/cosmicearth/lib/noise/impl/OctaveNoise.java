package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.OctaveNoiseSampler;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class OctaveNoise implements NoiseNode {
    private final NoiseNode noise;
    private final double lacunarity;
    private final double roughness;
    private final int detail;
    private final double distortion;

    public static void register() {
        NoiseLoader.registerNoiseNode("octave", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("octave transformer must have a `source`");

            int detail = options.getInt("detail", 2);
            double roughness = options.getDouble("roughness", 0.5d);
            double lacunarity = options.getDouble("lacunarity", 2d);
            double distortion = options.getDouble("distortion", 0d);


            return new OctaveNoise(
                    NoiseLoader.createNoiseNode(sourceObject),
                    detail, roughness, lacunarity, distortion
            );
        });
    }

    public OctaveNoise(NoiseNode noise, int detail, double roughness, double lacunarity, double distortion) {
        this.noise = noise;
        this.detail = detail;
        this.roughness = roughness;
        this.lacunarity = lacunarity;
        this.distortion = distortion;
    }

    @Override
    public double sample(double t) {
        return OctaveNoiseSampler.sample1D(
                noise, t,
                detail, roughness, lacunarity, distortion
        );
    }

    @Override
    public double sample(double x, double y) {
        return OctaveNoiseSampler.sample2D(
                noise, x, y,
                detail, roughness, lacunarity, distortion
        );
    }

    @Override
    public double sample(double x, double y, double z) {
        return OctaveNoiseSampler.sample3D(
                noise, x, y, z,
                detail, roughness, lacunarity, distortion
        );
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return OctaveNoiseSampler.sample4D(
                noise, x, y, z, w,
                detail, roughness, lacunarity, distortion
        );
    }

    @Override
    public void setSeed(long seed) {
        noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@OctaveNoise" + NoiseDebugString.createPropertyList(
            "detail", detail,
            "roughness", roughness,
            "lacunarity", lacunarity,
            "distortion", distortion
        ) + NoiseDebugString.buildStringSubnode(noise);
    }
}
