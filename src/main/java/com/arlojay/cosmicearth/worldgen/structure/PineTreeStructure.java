package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.world.Zone;

import java.util.Random;

public class PineTreeStructure extends WorldgenStructure {
    @Override
    protected String getId() {
        return "pine";
    }

    public void generate(Random random, Zone zone, int globalX, int globalY, int globalZ) {
        int treeHeight = random.nextInt(8, 11);

        double size = 1.0d;
        boolean small = true;

        for(int offsetY = treeHeight + 4; offsetY >= 4; offsetY--) {
            BuildHelper.placeDisk(zone, globalX, globalY + offsetY, globalZ, small ? (size / 2d) : (size), Palettes.instance.coniferous_leaves);
            size += 0.5;
            small = !small;
        }

        for(int offsetY = 0; offsetY <= treeHeight; offsetY++) {
            BuildHelper.setBlockState(zone, Palettes.instance.tree_log, globalX, globalY + offsetY, globalZ);
        }
    }
}
