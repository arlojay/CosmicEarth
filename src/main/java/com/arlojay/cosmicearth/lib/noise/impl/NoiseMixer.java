package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.MultiInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

import java.util.Arrays;

public class NoiseMixer extends MultiInputNoiseTransformer {
    public record MixerSource(NoiseNode noise, double factor) {}
    private static NoiseNode[] getSources(MixerSource[] sources) {
        return Arrays.stream(sources).map(source -> source.noise).toArray(NoiseNode[]::new);
    }

    public static void register() {
        NoiseLoader.registerNoiseNode("mix", (JsonObject options) -> {
            var sourceObjects = options.get("sources");
            if(sourceObjects == null) throw new NoSuchFieldException("mix transformer must have a `sources` node array");
            var sourceObjectArray = sourceObjects.asArray();

            var mixerSources = new MixerSource[sourceObjectArray.size()];

            for(int i = 0; i < mixerSources.length; i++) {
                var sourceObject = sourceObjectArray.get(i).asObject();
                double factor = sourceObject.getDouble("factor", 1.0d);
                var noise = sourceObject.get("noise");
                if(noise == null) throw new NoSuchFieldException("mix transformer sources must have a `noise` source");

                var sourceNode = NoiseLoader.createNoiseNode(noise);
                mixerSources[i] = new MixerSource(sourceNode, factor);
            }

            boolean normalize = options.getBoolean("normalize", true);

            return new NoiseMixer(mixerSources, normalize);
        });
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

    @Override
    public String buildString() {
        return "@NoiseMixer" + NoiseDebugString.createPropertyList(
                "sourceCount", sourceCount,
                "noiseFactorSum", noiseFactorSum,
                "normalize", normalize
        ) + super.buildString();
    }
}
