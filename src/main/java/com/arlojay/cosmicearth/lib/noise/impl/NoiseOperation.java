package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import org.hjson.JsonObject;

public class NoiseOperation implements NoiseNode {
    private final NoiseNode sourceA;
    private final NoiseNode sourceB;
    private final OperationType operation;

    private enum OperationType { ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER }

    public static void register() {
        NoiseLoader.registerNoiseNode("operation", (JsonObject options) -> {
            var sourceObjectA = options.get("a");
            if(sourceObjectA == null) throw new NoSuchFieldException("operation transformer must have source `a`");

            var sourceObjectB = options.get("b");
            if(sourceObjectB == null) throw new NoSuchFieldException("operation transformer must have source `b`");

            var operationString = options.getString("op", null);
            if(operationString == null) throw new NoSuchFieldException("operation transformer must have an `op` operator");
            var operation = OperationType.valueOf(operationString.toUpperCase());


            return new NoiseOperation(
                    NoiseLoader.createNoiseNode(sourceObjectA),
                    NoiseLoader.createNoiseNode(sourceObjectB),
                    operation
            );
        });
    }


    public NoiseOperation(NoiseNode sourceA, NoiseNode sourceB, OperationType operation) {
        this.sourceA = sourceA;
        this.sourceB = sourceB;
        this.operation = operation;
    }

    private double operate(double a, double b) {
        return switch (operation) {
            case ADD -> a + b;
            case SUBTRACT -> a - b;
            case MULTIPLY -> a * b;
            case DIVIDE -> a / b;
            case POWER -> Math.pow(a, b);
        };
    }

    @Override
    public double sample(double t) {
        return operate(sourceA.sample(t), sourceB.sample(t));
    }

    @Override
    public double sample(double x, double y) {
        return operate(sourceA.sample(x, y), sourceB.sample(x, y));
    }

    @Override
    public double sample(double x, double y, double z) {
        return operate(sourceA.sample(x, y, z), sourceB.sample(x, y, z));
    }

    @Override
    public double sample(double x, double y, double z, double w) {
        return operate(sourceA.sample(x, y, z, w), sourceB.sample(x, y, z, w));
    }

    @Override
    public void setSeed(long seed) {
        sourceA.setSeed(seed);
        sourceB.setSeed(seed);
    }

    @Override
    public String buildString() {
        return "@NoiseOperation" + NoiseDebugString.createPropertyList(
                "operation", operation.toString()
        ) + NoiseDebugString.buildStringSubnode(
                sourceA,
                sourceB
        );
    }
}
