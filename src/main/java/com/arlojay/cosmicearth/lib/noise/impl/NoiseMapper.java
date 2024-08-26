package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseMapper extends SingleInputNoiseTransformer {
    private final double outputMin;
    private final double outputMax;
    private final double inputMin;
    private final double inputMax;

    public static void register() {
        NoiseLoader.registerNoiseNode("map", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("map transformer must have a `source`");

            double inputMin = options.getDouble("inputMin", -1d);
            double inputMax = options.getDouble("inputMax", 1d);
            double outputMin = options.getDouble("outputMin", -1d);
            double outputMax = options.getDouble("outputMax", 1d);


            return new NoiseMapper(
                    NoiseLoader.createNoiseNode(sourceObject),
                    inputMin, inputMax, outputMin, outputMax
            );
        });
    }

    public NoiseMapper(NoiseNode source, double min, double max) {
        this(source, -1d, 1d, min, max);
    }

    public NoiseMapper(NoiseNode source, double inputMin, double inputMax, double outputMin, double outputMax) {
        super(source);
        this.inputMin = inputMin;
        this.inputMax = inputMax;
        this.outputMin = outputMin;
        this.outputMax = outputMax;
    }

    @Override
    protected double transform(double sample) {
        return (sample - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }

    @Override
    public String buildString() {
        return "@NoiseMapper" + NoiseDebugString.createPropertyList(
                "inputMin", this.inputMin,
                "inputMax", this.inputMax,
                "outputMin", this.outputMin,
                "outputMax", this.outputMax
        ) + super.buildString();
    }
}
