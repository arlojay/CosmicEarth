package com.arlojay.cosmicearth.worldgen.biome.util;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.noise.impl.generator.WhiteNoiseGenerator;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import finalforeach.cosmicreach.blocks.BlockState;

public abstract class SeededBlockGenerator implements BlockGenerator {
    protected final long seed;
    protected final NoiseNode noise;

    public SeededBlockGenerator(long seed) {
        this.seed = seed;
        this.noise = new WhiteNoiseGenerator(seed);
    }

    public abstract BlockState getBlock(int x, int y, int z, double gradient);
}
