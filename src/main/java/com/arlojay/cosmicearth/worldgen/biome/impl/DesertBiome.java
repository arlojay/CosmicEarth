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
import com.arlojay.cosmicearth.worldgen.structure.OakTreeStructure;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.List;

public class DesertBiome extends Biome {
    public DesertBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "desert";
    }

    @Override
    public BlockGenerator getTopsoilGenerator(long seed) {
        return new TopsoilGenerator(seed) {
            @Override
            public BlockState getBlock(int x, int y, int z, double gradient) {
                if(gradient < 2.0) return Palettes.instance.sand;
                return Palettes.instance.limestone;
            }
        };
    }

    @Override
    public BlockGenerator getLoamGenerator(long seed) {
        return topsoilGenerator;
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
