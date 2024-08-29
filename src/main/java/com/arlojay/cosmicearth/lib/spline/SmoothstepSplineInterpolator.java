package com.arlojay.cosmicearth.lib.spline;

public class SmoothstepSplineInterpolator extends SplineInterpolator<SmoothstepSplineInterpolator> {
    public SmoothstepSplineInterpolator(SplinePoint[] points) {
        super(points);
    }

    @Override
    protected double interpolate(double a, double b, double t) {
        return (b - a) * (t * t * (3d - 2d * t)) + a;
    }

    @Override
    protected SmoothstepSplineInterpolator makeNew(SplinePoint[] points) {
        return new SmoothstepSplineInterpolator(points);
    }
}
