package com.arlojay.cosmicearth.lib.spline;

public class ConstantSplineInterpolator extends SplineInterpolator<ConstantSplineInterpolator> {
    public ConstantSplineInterpolator(SplinePoint[] points) {
        super(points);
    }

    @Override
    protected double interpolate(double a, double b, double t) {
        return t > 0.5 ? b : a;
    }

    @Override
    protected ConstantSplineInterpolator makeNew(SplinePoint[] points) {
        return new ConstantSplineInterpolator(points);
    }
}