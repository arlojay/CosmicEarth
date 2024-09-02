package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.SeededBlockGenerator;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.List;

public class RoughlandsBiome extends Biome {
    public RoughlandsBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "roughlands";
    }

    @Override
    protected void addStructures(List<BiomeStructure> structures) { }

    @Override
    protected BlockGenerator getTopsoilGenerator(long seed) {
        return new SeededBlockGenerator(seed) {
            @Override
            public BlockState getBlock(int x, int y, int z, double gradient) {
                if(gradient < 1.5) return Palettes.instance.limestone;
                if(gradient < 2.5) return Palettes.instance.gravel;
                return Palettes.instance.stone;
            }
        };
    }

    @Override
    protected BlockGenerator getLoamGenerator(long seed) {
        return getTopsoilGenerator(seed);
    }
}
