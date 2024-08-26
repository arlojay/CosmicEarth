package com.arlojay.cosmicearth.lib.spline;

public class SplineMapper {
    public enum Interpolator {
        SMOOTHSTEP() {
            public double interpolate(double a, double b, double t) {
                return (b - a) * (t * t * (3d - 2d * t)) + a;
            }
        },
        LINEAR() {
            public double interpolate(double a, double b, double t) {
                return (b - a) * t + a;
            }
        },
        CONSTANT() {
            public double interpolate(double a, double b, double t) {
                return t > 0.5 ? b : a;
            }
        };

        public double interpolate(double a, double b, double t) {
            return 0;
        }
    }

    private final SplinePoint[] points;
    private final Interpolator interpolator;

    public SplineMapper(SplinePoint[] unsortedPoints, Interpolator interpolator) {
        this.points = unsortedPoints; // too lazy lmao
        this.interpolator = interpolator;
    }

    public double transform(double sample) {
        var lastPoint = points[0];
        if(sample <= lastPoint.x) return lastPoint.y;

        for (var currentPoint : points) {
            if (currentPoint.x >= sample) return interpolator.interpolate(
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
