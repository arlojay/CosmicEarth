package com.arlojay.cosmicearth.worldgen.biome.util;

import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

public class NaturalLoamGenerator extends LoamGenerator {
    public NaturalLoamGenerator(long seed) {
        super(seed);
    }

    public BlockState getBlock(int x, int y, int z, double gradient) {
        if(gradient < 1.5) return Palettes.instance.dirt;
        if(gradient < 2.5) return Palettes.instance.steepGradientLoam.getItem(
                noise.sample(x, y, z) * Double.MAX_VALUE
        );
        return Palettes.instance.stone;
    }
}
