package com.arlojay.cosmicearth.lib.spline;

public class LinearSplineInterpolator extends SplineInterpolator<LinearSplineInterpolator> {
    public LinearSplineInterpolator(SplinePoint[] points) {
        super(points);
    }

    @Override
    protected double interpolate(double a, double b, double t) {
        return (b - a) * t + a;
    }

    @Override
    protected LinearSplineInterpolator makeNew(SplinePoint[] points) {
        return new LinearSplineInterpolator(points);
    }
}
