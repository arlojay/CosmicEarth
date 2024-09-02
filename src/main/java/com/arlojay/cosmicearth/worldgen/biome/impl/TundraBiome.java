package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.biome.util.TundraGenerator;

import java.util.List;

public class TundraBiome extends Biome {
    public TundraBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "tundra";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new TundraGenerator(seed);
    }

    @Override
    public BlockGenerator getLoamGenerator(long seed) {
        return new TundraGenerator(seed);
    }

    @Override
    protected void addStructures(List<BiomeStructure> structures) {
        StructureSet.pebbles(structures);
    }
}
