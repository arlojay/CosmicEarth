package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.SeededBlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import finalforeach.cosmicreach.blocks.BlockState;

import java.util.List;

public class ShoreBiome extends Biome {
    public ShoreBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "shore";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new SeededBlockGenerator(seed) {
            @Override
            public BlockState getBlock(int x, int y, int z, double gradient) {
                return Palettes.instance.sand;
            }
        };
    }

    @Override
    public BlockGenerator getLoamGenerator(long seed) {
        return getTopsoilGenerator(seed);
    }

    @Override
    protected void addStructures(List<BiomeStructure> structures) {
        StructureSet.pebbles(structures);
    }
}
