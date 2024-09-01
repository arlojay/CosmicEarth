package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.*;
import com.arlojay.cosmicearth.worldgen.structure.*;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.List;

public class PlainsBiome extends Biome {
    public PlainsBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "plains";
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
        StructureSet.pebbles(structures);
        StructureSet.flowers(structures, new BlockState[] {
                Palettes.instance.rose,
                Palettes.instance.red_pansy,
                Palettes.instance.white_mimosa,
                Palettes.instance.daisy,
                Palettes.instance.orchid
        });

        StructureSet.shortGrass(structures);
        StructureSet.tallGrass(structures);
        StructureSet.shrub(structures);


        structures.add(new BiomeStructure() {
            public double getAverageDistance() {
                return 80;
            }
            public WorldgenStructure getWorldgenStructure() {
                return new OakTreeStructure();
            }
            public Range getGradientRange() {
                return new Range(0d, 0.2d);
            }
            public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
                return ground.hasTag("soil_tropical") && air.hasTag("foliage_replaceable");
            }
        });


        structures.add(new BiomeStructure() {
            public double getAverageDistance() {
                return 50;
            }
            public WorldgenStructure getWorldgenStructure() {
                return new HickoryTreeStructure();
            }
            public Range getGradientRange() {
                return new Range(0.3d, 2.2d);
            }
            public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
                return ground.hasTag("soil_tropical") && air.hasTag("foliage_replaceable");
            }
        });
    }
}
