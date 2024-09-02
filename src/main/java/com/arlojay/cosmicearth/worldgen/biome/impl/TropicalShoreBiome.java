package com.arlojay.cosmicearth.worldgen.biome.impl;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.biome.Biome;
import com.arlojay.cosmicearth.worldgen.biome.BiomeStructure;
import com.arlojay.cosmicearth.worldgen.biome.BlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.SeededBlockGenerator;
import com.arlojay.cosmicearth.worldgen.biome.util.StructureSet;
import com.arlojay.cosmicearth.worldgen.structure.Palettes;
import com.arlojay.cosmicearth.worldgen.structure.PalmTreeStructure;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.List;

public class TropicalShoreBiome extends Biome {
    public TropicalShoreBiome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        super(seed, temperature, humidity, erosion, continent);
    }

    @Override
    public String getName() {
        return "tropical_shore";
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


        structures.add(new BiomeStructure() {
            public double getAverageDistance() {
                return 40;
            }
            public WorldgenStructure getWorldgenStructure() {
                return new PalmTreeStructure();
            }
            public Range getGradientRange() {
                return new Range(0d, 0.75d);
            }
            public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
                return ground.equals(Palettes.instance.sand) && air.hasTag("foliage_replaceable");
            }
        });
    }
}
