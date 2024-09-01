package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.Random;

public class CactusStructure extends WorldgenStructure {
    private final WhiteNoise randomNoise = new WhiteNoise();
    private final Random random = new Random();

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        randomNoise.setSeed(seed);
        var randseed = (double) randomNoise.noise3D((float) (globalX - 81.40580330862556), (float) (globalY + 143.77008354155828), (float) (globalZ - 7.437984677149956));
        random.setSeed(Double.doubleToLongBits(randseed));

        var height = random.nextInt(2, 5);

        for(int dy = 0; dy < height; dy++) {
            BuildHelper.setBlockState(zone, Palettes.instance.cactus, globalX, globalY + dy, globalZ);
        }
    }
}
