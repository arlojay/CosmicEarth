package com.arlojay.cosmicearth.worldgen.biome;

import finalforeach.cosmicreach.blocks.BlockState;

public interface BlockGenerator {
    BlockState getBlock(int x, int y, int z, double gradient);
}
