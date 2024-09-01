package com.arlojay.cosmicearth.lib;

public class Range {
    public final double min;
    public final double max;

    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public boolean isWithin(double gradient) {
        return gradient <= this.max && gradient >= this.min;
    }

    public double getMiddle() {
        return (min + max) / 2d;
    }
}
