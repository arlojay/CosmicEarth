package com.arlojay.cosmicearth.worldgen.structure;

import com.arlojay.cosmicearth.lib.variety.Palette;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

public class RockStructure extends WorldgenStructure {
    private final Palette<BlockState> blocks;

    public RockStructure(Palette<BlockState> blocks) {
        this.blocks = blocks;
    }

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {

    }
}
