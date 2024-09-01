package com.arlojay.cosmicearth.worldgen.biome.util;

import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

public class NaturalTopsoilGenerator extends TopsoilGenerator {
    public NaturalTopsoilGenerator(long seed) {
        super(seed);
    }

    public BlockState getBlock(int x, int y, int z, double gradient) {
        if(gradient < 0.5) return Palettes.instance.grass_full;
        if(gradient < 1.5) return Palettes.instance.grass;
        if(gradient < 2.5) return Palettes.instance.steepGradientTopsoil.getItem(
                noise.sample(x, y, z) * Double.MAX_VALUE
        );
        return Palettes.instance.stone;
    }
}
