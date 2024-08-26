package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseDebugString;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.lib.spline.SplineMapper;
import com.arlojay.cosmicearth.lib.spline.SplinePoint;
import org.hjson.JsonObject;

public class NoiseSpline extends SingleInputNoiseTransformer {
    private final SplineMapper mapper;
    private final SplineMapper.Interpolator interpolator;

    public static void register() {
        NoiseLoader.registerNoiseNode("graph", (JsonObject options) -> {
            var sourceObject = options.get("source");
            if(sourceObject == null) throw new NoSuchFieldException("graph transformer must have a `source`");

            var pointObjects = options.get("points");
            if(pointObjects == null) throw new NoSuchFieldException("graph transformer must have a `points` array");
            var pointObjectsArray = pointObjects.asArray();

            var interpolationTypeString = options.getString("interpolator", SplineMapper.Interpolator.SMOOTHSTEP.toString());
            var interpolator = SplineMapper.Interpolator.valueOf(interpolationTypeString.toUpperCase());

            var mixerSources = new SplinePoint[pointObjectsArray.size()];

            for(int i = 0; i < mixerSources.length; i++) {
                var pointObject = pointObjectsArray.get(i).asArray();

                double x = pointObject.get(0).asDouble();
                double y = pointObject.get(1).asDouble();

                mixerSources[i] = new SplinePoint(x, y);
            }

            return new NoiseSpline(
                    NoiseLoader.createNoiseNode(sourceObject),
                    new SplineMapper(mixerSources, interpolator),
                    interpolator
            );
        });
    }

    public NoiseSpline(NoiseNode source, SplineMapper mapper) {
        this(source, mapper, SplineMapper.Interpolator.SMOOTHSTEP);
    }

    public NoiseSpline(NoiseNode source, SplineMapper mapper, SplineMapper.Interpolator interpolationType) {
        super(source);
        this.mapper = mapper;
        this.interpolator = interpolationType;
    }

    @Override
    protected double transform(double sample) {
        return mapper.transform(sample);
    }

    @Override
    public String buildString() {
        return "@NoiseSpline" + NoiseDebugString.createPropertyList(
                "interpolator", interpolator.toString(),
                "mapper", mapper.toString()
        ) + super.buildString();
    }
}
