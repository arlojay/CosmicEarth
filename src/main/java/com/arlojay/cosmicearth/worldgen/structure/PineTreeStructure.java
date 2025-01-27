package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

import java.util.Random;

public class PineTreeStructure extends WorldgenStructure {
    private static final BlockState treeLogBlock = getBlockStateInstance("cosmicearth:pine_log[default]");
    private static final BlockState leavesBlock = getBlockStateInstance("cosmicearth:pine_leaves[default]");

    @Override
    protected String getId() {
        return "pine";
    }

    public void generate(Random random, Zone zone, int globalX, int globalY, int globalZ) {
        int treeHeight = random.nextInt(8, 11);

        double size = 1.0d;
        boolean small = true;

        for(int offsetY = treeHeight + 4; offsetY >= 4; offsetY--) {
            BuildHelper.placeDisk(zone, globalX, globalY + offsetY, globalZ, small ? (size / 2d) : (size), leavesBlock);
            size += 0.5;
            small = !small;
        }

        for(int offsetY = 0; offsetY <= treeHeight; offsetY++) {
            BuildHelper.setBlockState(zone, treeLogBlock, globalX, globalY + offsetY, globalZ);
        }
    }
}
