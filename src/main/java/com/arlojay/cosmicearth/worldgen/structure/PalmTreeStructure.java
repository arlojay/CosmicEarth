package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;
import finalforeach.cosmicreach.worldgen.trees.CoconutTree;

public class PalmTreeStructure extends WorldgenStructure {
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        CoconutTree.generateTree(seed, zone, globalX, globalY, globalZ);
    }
}
