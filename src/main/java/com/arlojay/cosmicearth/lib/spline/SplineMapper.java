package com.arlojay.cosmicearth.lib.spline;

public class SplineMapper {
    private final SplinePoint[] points;

    public SplineMapper(SplinePoint[] unsortedPoints) {
        this.points = unsortedPoints; // too lazy lmao
    }

    private double interpolate(double a, double b, double t) {
        return (b - a) * (t * t * (3d - 2d * t)) + a;
    }

    public double interpolate(double sample) {
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

    public String toString() {
        var builder = new StringBuilder();
        boolean first = true;
        for(var point : points) {
            if(!first) builder.append(", ");
            builder.append("(").append(point.x).append(", ").append(point.y).append(")");
            first = false;
        }

        return builder.toString();
    }
}
