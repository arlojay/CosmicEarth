package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;

import java.util.function.Function;

public abstract class FoliageClusterStructure extends ClusterStructure {
    public FoliageClusterStructure(BlockState blockType, double minRadius, double maxRadius, double density) {
        super(blockType, minRadius, maxRadius, density);
    }

    public Function<BlockState, Boolean> createGroundChecker() {
        return bs -> bs.hasTag("soil_temperate");
    }
    public Function<BlockState, Boolean> createAirChecker() {
        return bs -> bs.hasTag("foliage_replaceable");
    }
}
