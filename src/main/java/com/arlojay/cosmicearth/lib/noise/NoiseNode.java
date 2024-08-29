package com.arlojay.cosmicearth.lib.noise;

import com.arlojay.cosmicearth.lib.Copyable;

public interface NoiseNode extends Copyable<NoiseNode> {
    double sample(double t);
    double sample(double x, double y);
    double sample(double x, double y, double z);
    double sample(double x, double y, double z, double w);

    NoiseNode asCopy();

    void setSeed(long seed);
    String buildString();
}
