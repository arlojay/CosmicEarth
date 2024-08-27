package com.arlojay.cosmicearth.lib.spline;


public class SplinePoint {
    public double y;
    public double x;

    public SplinePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public SplinePoint asCopy() {
        return new SplinePoint(x, y);
    }
}