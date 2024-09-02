package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.world.Zone;

import java.util.Random;

public class CactusStructure extends WorldgenStructure {
    @Override
    protected String getId() {
        return "cactus";
    }

    @Override
    public void generate(Random random, Zone zone, int globalX, int globalY, int globalZ) {
        var height = random.nextInt(2, 5);

        for(int dy = 0; dy < height; dy++) {
            BuildHelper.setBlockState(zone, Palettes.instance.cactus, globalX, globalY + dy, globalZ);
        }
    }
}
