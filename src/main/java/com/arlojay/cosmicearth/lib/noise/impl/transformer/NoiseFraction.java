package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseFraction extends SingleInputNoiseTransformer {
    public static void register() {
        NoiseLoader.registerNoiseNode("fract", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("fraction transformer must have a `source`");

            return new NoiseFraction(
                    NoiseLoader.createNoiseNode(sourceObject)
            );
        });
    }


    public NoiseFraction(NoiseNode source) {
        super(source);
    }

    public NoiseFraction asCopy() {
        return new NoiseFraction(source.asCopy());
    }

    @Override
    protected double transform(double sample) {
        return (sample) - ((long) sample);
    }

    @Override
    public String buildString() {
        return "@NoiseFraction" + NoiseDebugString.createPropertyList() + super.buildString();
    }
}
