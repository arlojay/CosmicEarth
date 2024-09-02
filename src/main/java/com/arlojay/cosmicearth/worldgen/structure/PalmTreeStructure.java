package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.trees.CoconutTree;

import java.util.Random;

public class PalmTreeStructure extends WorldgenStructure {
    @Override
    protected String getId() {
        return "palm";
    }

    public void generate(Random random, Zone zone, int globalX, int globalY, int globalZ) {
        CoconutTree.generateTree(random.nextLong(), zone, globalX, globalY, globalZ);
    }
}
