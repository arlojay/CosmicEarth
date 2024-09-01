package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

public class HickoryTreeStructure extends WorldgenStructure {
    static BlockState treeLogBlock = getBlockStateInstance("base:tree_log[default]");
    static BlockState leavesBlock = getBlockStateInstance("cosmicearth:deciduous_leaves[default]");
    static WhiteNoise whiteNoiseTreesH = new WhiteNoise();

    protected static BlockState getBlockStateInstance(String blockStateId) {
        return BlockState.getInstance(blockStateId);
    }

    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        whiteNoiseTreesH.setSeed(seed);

        int treeHeight = (int) ((double) whiteNoiseTreesH.noise3D(globalX, globalY, globalZ) * 2d + 8d);

        double size = 1.0d;

        for(int offsetY = treeHeight + 4; offsetY >= 4; offsetY--) {
            BuildHelper.placeDisk(zone, globalX, globalY + offsetY, globalZ, Math.pow(size, 0.6), leavesBlock);
            size += 0.5;
        }

        for(int offsetY = 0; offsetY <= treeHeight; offsetY++) {
            BuildHelper.setBlockState(zone, treeLogBlock, globalX, globalY + offsetY, globalZ);
        }
    }
}
