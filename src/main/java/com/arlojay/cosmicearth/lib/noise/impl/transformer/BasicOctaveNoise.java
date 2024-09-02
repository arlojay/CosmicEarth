package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

import java.util.Arrays;

public class BasicOctaveNoise implements NoiseNode {
    private final NoiseNode noise;
    private final double[] amplitudes;

    public static void register() {
        NoiseLoader.registerNoiseNode("basic-octave", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("basic-octave transformer must have a `source`");

            var amplitudeArray = options.get("amplitudes").asArray();
            var amplitudes = new double[amplitudeArray.size()];
            for(int i = 0; i < amplitudes.length; i++) amplitudes[i] = amplitudeArray.get(i).asDouble();


            return new BasicOctaveNoise(
                    NoiseLoader.createNoiseNode(sourceObject),
                    amplitudes
            );
        });
    }

    public BasicOctaveNoise(NoiseNode noise, double[] amplitudes) {
        this.noise = noise;
        this.amplitudes = amplitudes;
    }

    public BasicOctaveNoise asCopy() {
        return new BasicOctaveNoise(noise.asCopy(), Arrays.copyOf(amplitudes, amplitudes.length));
    }

    @Override
    public double sample(double t) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(var amplitude : amplitudes) {
            max += effectiveness;
            if(amplitude != 0.0d) value += noise.sample(t * scale) * effectiveness * amplitude;

            effectiveness *= 0.5;
            scale *= 2.0;
        }
        return value / max;
    }

    @Override
    public double sample(double x, double y) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(var amplitude : amplitudes) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale) * effectiveness * amplitude;

            effectiveness *= 0.5;
            scale *= 2.0;
        }
        return value / max;
    }

    @Override
    public double sample(double x, double y, double z) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(var amplitude : amplitudes) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale, z * scale) * effectiveness * amplitude;

            effectiveness *= 0.5;
            scale *= 2.0;
        }
        return value / max;
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        double max = 0.0;
        double value = 0.0;
        double effectiveness = 1.0;
        double scale = 1.0;
        for(var amplitude : amplitudes) {
            max += effectiveness;
            value += noise.sample(x * scale, y * scale,z * scale, w * scale) * effectiveness * amplitude;

            effectiveness *= 0.5;
            scale *= 2.0;
        }
        return value / max;
    }

    @Override
    public void setSeed(long seed) {
        this.noise.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@BasicOctaveNoise" + NoiseDebugString.createPropertyList(
                "amplitudes", Arrays.toString(amplitudes)
        ) + NoiseDebugString.buildStringSubnode(noise);
    }
}
