package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;

public class CustomNoiseTransformer extends SingleInputNoiseTransformer {
    public interface Transformer {
        double transform(double sample);
    }

    private final Transformer transformer;
    public CustomNoiseTransformer(NoiseNode source, Transformer transformer) {
        super(source);
        this.transformer = transformer;
    }

    @Override
    protected double transform(double sample) {
        return transformer.transform(sample);
    }

    @Override
    public String buildString() {
        return "@CustomNoiseTransformer" + NoiseDebugString.createPropertyList() + super.buildString();
    }
}
