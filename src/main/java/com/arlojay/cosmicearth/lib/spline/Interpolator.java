package com.arlojay.cosmicearth.lib.spline;

import java.util.function.Function;

public enum Interpolator {
    SMOOTHSTEP(SmoothstepSplineInterpolator::new),
    LINEAR(LinearSplineInterpolator::new),
    CONSTANT(ConstantSplineInterpolator::new),
    CUBIC(CubicSplineInterpolator::new);

    private final Function<SplinePoint[], SplineInterpolator<?>> factory;

    Interpolator(Function<SplinePoint[], SplineInterpolator<?>> factory) {
        this.factory = factory;
    }
    public SplineInterpolator<?> create(SplinePoint[] points) {
        return factory.apply(points);
    }
}