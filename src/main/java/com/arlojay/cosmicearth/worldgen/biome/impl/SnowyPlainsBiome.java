package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalLoamGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalTopsoilGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.biome.util.TopsoilGenerator;
import com.arlojay.cosmicearth.worldgen.structure.CactusStructure;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.List;

public class SnowyPlainsBiome extends Biome {
    public SnowyPlainsBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "snowy_plains";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new TopsoilGenerator(seed) {
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
        structures.add(new BiomeStructure() {
            protected double getAverageDistance() {
                return 32;
            }
            protected WorldgenStructure getWorldgenStructure() {
                return new CactusStructure();
            }
            public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
                return ground.equals(Palettes.instance.sand) && air.hasTag("foliage_replaceable");
            }
        });
    }
}
