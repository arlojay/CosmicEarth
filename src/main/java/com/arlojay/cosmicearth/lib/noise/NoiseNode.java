package com.arlojay.cosmicearth.lib.noise;

public interface NoiseNode {
    double sample(double t);
    double sample(double x, double y);
    double sample(double x, double y, double z);
    double sample(double x, double y, double z, double w);
}
