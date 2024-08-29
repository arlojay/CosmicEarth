package com.arlojay.cosmicearth.lib.noise.impl.transformer;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.lib.spline.Interpolator;
import com.arlojay.cosmicearth.lib.spline.SplineInterpolator;
import com.arlojay.cosmicearth.lib.spline.SplinePoint;
import org.hjson.JsonObject;

public class NoiseSpline extends SingleInputNoiseTransformer {
    private final Interpolator interpolatorType;
    private final SplineInterpolator<?> interpolator;

    public static void register() {
        NoiseLoader.registerNoiseNode("graph", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("graph transformer must have a `source`");

            var pointObjects = options.get("points");
            if(pointObjects == null) throw new NoSuchFieldException("graph transformer must have a `points` array");
            var pointObjectsArray = pointObjects.asArray();

            var interpolationTypeString = options.getString("interpolator", Interpolator.SMOOTHSTEP.toString());
            var interpolator = Interpolator.valueOf(interpolationTypeString.toUpperCase());

            var mixerSources = new SplinePoint[pointObjectsArray.size()];

            for(int i = 0; i < mixerSources.length; i++) {
                var pointObject = pointObjectsArray.get(i).asArray();

                double x = pointObject.get(0).asDouble();
                double y = pointObject.get(1).asDouble();

                mixerSources[i] = new SplinePoint(x, y);
            }

            return new NoiseSpline(
                    NoiseLoader.createNoiseNode(sourceObject),
                    interpolator.create(mixerSources),
                    interpolator
            );
        });
    }

    public NoiseSpline(NoiseNode source, SplineInterpolator<?> interpolator, Interpolator interpolatorType) {
        super(source);
        this.interpolator = interpolator;
        this.interpolatorType = interpolatorType;
    }

    @Override
    public NoiseSpline asCopy() {
        return new NoiseSpline(source.asCopy(), interpolator.asCopy(), interpolatorType);
    }

    @Override
    protected double transform(double sample) {
        return interpolator.interpolate(sample);
    }

    @Override
    public String buildString() {
        return "@NoiseSpline" + NoiseDebugString.createPropertyList(
                "interpolatorType", interpolatorType.toString(),
                "interpolator", interpolator.toString()
        ) + super.buildString();
    }
}
