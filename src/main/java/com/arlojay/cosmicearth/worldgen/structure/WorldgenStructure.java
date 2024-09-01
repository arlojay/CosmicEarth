package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

public abstract class WorldgenStructure {
    protected static BlockState getBlockStateInstance(String blockStateId) {
        return BlockState.getInstance(blockStateId);
    }

    public abstract void generate(long seed, Zone zone, int globalX, int globalY, int globalZ);
}
