package com.arlojay.cosmicearth.worldgen.structure;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.lib.variety.Palette;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.Random;

public abstract class RockStructure extends WorldgenStructure {
    private final Palette<BlockState> blocks;
    private final Range size;

    public RockStructure(Palette<BlockState> blocks, Range size) {
        this.blocks = blocks;
        this.size = size;
    }

    @Override
    public void generate(Random random, Zone zone, int globalX, int globalY, int globalZ) {

    }
}
