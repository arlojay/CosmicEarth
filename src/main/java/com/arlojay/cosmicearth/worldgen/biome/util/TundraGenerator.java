package com.arlojay.cosmicearth.worldgen.biome.util;

import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

public class TundraGenerator extends SeededBlockGenerator {
    public TundraGenerator(long seed) {
        super(seed);
    }

    @Override
    public BlockState getBlock(int x, int y, int z, double gradient) {
        return Palettes.instance.tundraSoil.getItem(noise.sample(x, y, z) + gradient);
    }
}
