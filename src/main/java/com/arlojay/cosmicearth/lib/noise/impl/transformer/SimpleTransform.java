package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

import java.util.Arrays;

public class SimpleTransform extends SingleInputNoiseTransformer {
    public enum SimpleTransformType { SMOOTHSTEP }

    private final SimpleTransformType operation;

    public static void register() {
        NoiseLoader.registerNoiseNode("simpleTransform", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("simple transform transformer must have a `source`");

            return new SimpleTransform(
                    NoiseLoader.createNoiseNode(sourceObject),
                    SimpleTransformType.valueOf(options.getString("operation", "SMOOTHSTEP").toUpperCase())
            );
        });
    }


    public SimpleTransform(NoiseNode source, SimpleTransformType operation) {
        super(source);

        this.operation = operation;
    }

    public SimpleTransform asCopy() {
        return new SimpleTransform(source.asCopy(), operation);
    }

    @Override
    protected double transform(double sample) {
        return Math.abs(sample);
    }

    @Override
    public String buildString() {
        return "@SimpleTransform" + NoiseDebugString.createPropertyList(
                "operation", operation.toString()
        ) + super.buildString();
    }
}
