package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseAbsolute extends SingleInputNoiseTransformer {
    public static void register() {
        NoiseLoader.registerNoiseNode("absolute", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("absolute transformer must have a `source`");

            JsonObject sourceNode = sourceObject.asObject();

            return new NoiseAbsolute(
                    NoiseLoader.createNoiseNode(sourceNode)
            );
        });
    }


    public NoiseAbsolute(NoiseNode source) {
        super(source);
    }

    @Override
    protected double transform(double sample) {
        return Math.abs(sample);
    }

    @Override
    public String buildString() {
        return "@NoiseAbsolute" + NoiseDebugString.createPropertyList() + super.buildString();
    }
}
