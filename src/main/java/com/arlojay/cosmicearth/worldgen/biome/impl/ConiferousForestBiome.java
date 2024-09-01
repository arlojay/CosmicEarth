package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalLoamGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.NaturalTopsoilGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.structure.PineTreeStructure;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.List;

public class ConiferousForestBiome extends Biome {
    public ConiferousForestBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "coniferous_forest";
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
                Palettes.instance.yellow_tulip,
                Palettes.instance.orange_tulip,
                Palettes.instance.red_tulip,
                Palettes.instance.orange_pansy,
        });

        StructureSet.shortGrass(structures);
        StructureSet.tallGrass(structures);


        structures.add(new BiomeStructure() {
            public double getAverageDistance() {
                return 12;
            }
            public WorldgenStructure getWorldgenStructure() {
                return new PineTreeStructure();
            }
            public Range getGradientRange() {
                return new Range(0d, 2.0d);
            }
            public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
                return ground.hasTag("soil_temperate") && air.hasTag("foliage_replaceable");
            }
        });
    }
}
