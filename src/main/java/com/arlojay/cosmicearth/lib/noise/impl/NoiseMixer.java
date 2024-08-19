package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.MultiInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;

import java.util.Arrays;

public class NoiseMixer extends MultiInputNoiseTransformer {
    public record MixerSource(NoiseNode noise, double factor) {}
    private static NoiseNode[] getSources(MixerSource[] sources) {
        return Arrays.stream(sources).map(source -> source.noise).toArray(NoiseNode[]::new);
    }

    private final double[] noiseFactors;
    private final double noiseFactorSum;
    private final boolean normalize;

    public NoiseMixer(MixerSource[] sources) {
        this(sources, true);
    }

    public NoiseMixer(MixerSource[] sources, boolean normalize) {
        super(getSources(sources));

        this.normalize = normalize;
        this.noiseFactors = new double[sources.length];
        double _noiseFactorSum = 0d;

        for(int i = 0; i < sources.length; i++) {
            _noiseFactorSum += noiseFactors[i] = sources[i].factor;
        }

        noiseFactorSum = _noiseFactorSum;
    }

    @Override
    protected double transform(double[] samples) {
        double total = 0d;
        for (int i = 0; i < sourceCount; i++) total += samples[i] * noiseFactors[i];

        return normalize ? total / noiseFactorSum : total;
    }
}
