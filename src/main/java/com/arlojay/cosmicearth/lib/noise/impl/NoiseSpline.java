package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.SingleInputNoiseTransformer;

import java.util.Arrays;

public class NoiseSpline extends SingleInputNoiseTransformer {
    public record SplinePoint(double x, double y) {}

    private final SplinePoint[] points;

    public NoiseSpline(NoiseNode source, SplinePoint[] unsortedPoints) {
        super(source);
        this.points = unsortedPoints; // too lazy lmao
    }

    private double interpolate(double a, double b, double t) {
        return (b - a) * (t * t * (3d - 2d * t)) + a;
    }

    @Override
    protected double transform(double sample) {
        var lastPoint = points[0];
        for (var currentPoint : points) {
            if (currentPoint.x > sample) return interpolate(
                lastPoint.y, currentPoint.y,
                (sample - lastPoint.x) / (currentPoint.x - lastPoint.x)
            );
            lastPoint = currentPoint;
        }
        return points[points.length - 1].y;
    }
}
