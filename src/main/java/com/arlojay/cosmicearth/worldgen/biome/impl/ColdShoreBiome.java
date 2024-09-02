package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalLoamGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.SeededBlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.List;

public class ColdShoreBiome extends Biome {
    public ColdShoreBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "cold_shore";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new SeededBlockGenerator(seed) {
            @Override
            public BlockState getBlock(int x, int y, int z, double gradient) {
                return Palettes.instance.snow;
            }
        };
    }

    @Override
    public BlockGenerator getLoamGenerator(long seed) {
        return new NaturalLoamGenerator(seed);
    }

    @Override
    protected void addStructures(List<BiomeStructure> structures) {
        StructureSet.pebbles(structures);
    }
}
