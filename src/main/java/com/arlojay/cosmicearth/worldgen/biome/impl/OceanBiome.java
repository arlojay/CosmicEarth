package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalLoamGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalTopsoilGenerator;

import java.util.List;

public class OceanBiome extends Biome {
    public OceanBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "ocean";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new NaturalTopsoilGenerator(seed);
    }

    @Override
    public BlockGenerator getLoamGenerator(long seed) {
        return new NaturalLoamGenerator(seed);
    }

    @Override
    protected void addStructures(List<BiomeStructure> structures) {

    }
}
