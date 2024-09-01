package com.arlojay.cosmicearth.worldgen.biome;

import com.arlojay.cosmicearth.worldgen.structure.ClusterStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

public abstract class ClusterBiomeStructure extends BiomeStructure {
    @Override
    public boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air) {
        var structure = ((ClusterStructure) worldgenStructure);
        return structure.groundChecker.apply(ground) && structure.airChecker.apply(air);
    }
}