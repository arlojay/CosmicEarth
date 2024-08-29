package com.arlojay.cosmicearth.lib.spline;

import com.arlojay.cosmicearth.lib.Copyable;

public abstract class SplineInterpolator<Implementer extends SplineInterpolator<Implementer>> implements Copyable<SplineInterpolator<Implementer>> {
    protected final SplinePoint[] points;

    public SplineInterpolator(SplinePoint[] points) {
        this.points = points;
    }
    protected double interpolate(double a, double b, double t) {
        return 0;
    }
    public double interpolate(double sample) {
        var lastPoint = points[0];
        if(sample <= lastPoint.x) return lastPoint.y;

        for (var currentPoint : points) {
            if (currentPoint.x >= sample) return interpolate(
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

    public Implementer asCopy() {
        var pointClones = new SplinePoint[points.length];
        for(int i = 0; i < pointClones.length; i++) pointClones[i] = points[i].asCopy();
        return makeNew(pointClones);
    }

    protected abstract Implementer makeNew(SplinePoint[] points);
}
