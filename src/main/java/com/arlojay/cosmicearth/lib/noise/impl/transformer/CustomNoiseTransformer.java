package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;

import java.util.function.Function;

public class CustomNoiseTransformer extends SingleInputNoiseTransformer {
    private final Function<Double, Double> transformer;
    public CustomNoiseTransformer(NoiseNode source, Function<Double, Double> transformer) {
        super(source);
        this.transformer = transformer;
    }

    public CustomNoiseTransformer asCopy() {
        return new CustomNoiseTransformer(source.asCopy(), transformer);
    }

    @Override
    protected double transform(double sample) {
        return transformer.apply(sample);
    }

    @Override
    public String buildString() {
        return "@CustomNoiseTransformer" + NoiseDebugString.createPropertyList() + super.buildString();
    }
}
