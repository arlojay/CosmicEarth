package com.arlojay.cosmicearth.lib.noise.impl;

import com.arlojay.cosmicearth.lib.noise.NoiseGenerator;

import java.util.Random;

public class WhiteNoiseGenerator extends NoiseGenerator {
    private final Random noise;
    private final double[] randomTable;

    public WhiteNoiseGenerator(long seed) {
        super(seed);

        this.noise = new Random(seed);

        randomTable = new double[256];
        for(int i = 0; i < randomTable.length; i++) randomTable[i] = this.noise.nextDouble() * 10000000D;
    }

    private double sampleRandomTable(double n) {
        return randomTable[(int) ((Double.doubleToLongBits(n) / n)) & 0xff];
    }

    public double sample(double t) {
        this.noise.setSeed(seed + Double.doubleToLongBits(t + sampleRandomTable(t)));
        return this.noise.nextDouble() * 2d - 1d;
    }

    public double sample(double x, double y) {
        double n = sample(x);
        this.noise.setSeed(seed + Double.doubleToLongBits(y + sampleRandomTable(n)));
        return this.noise.nextDouble() * 2d - 1d;
    }

    public double sample(double x, double y, double z) {
        double n = sample(x, y);
        this.noise.setSeed(seed + Double.doubleToLongBits(z + sampleRandomTable(n)));
        return this.noise.nextDouble() * 2d - 1d;
    }

    public double sample(double x, double y, double z, double w) {
        return sample(sample(x, y) * sampleRandomTable(z + w), sample(z, w) * sampleRandomTable(x + y)) * 2d - 1d;
    }
}
