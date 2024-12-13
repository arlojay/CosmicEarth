package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.Objects;
import java.util.Random;

import static finalforeach.cosmicreach.blocks.MissingBlockStateResult.EXCEPTION;

public abstract class WorldgenStructure {
    private final WhiteNoise randomNoise = new WhiteNoise();
    private final Random random = new Random();

    private float offsetX;
    private float offsetY;
    private float offsetZ;

    public WorldgenStructure() {
        offsetX = Float.intBitsToFloat(Objects.hash(this.getId()));
        offsetY = Float.intBitsToFloat(Objects.hash(this.getId(), offsetX));
        offsetZ = Float.intBitsToFloat(Objects.hash(this.getId(), offsetY));

        offsetX %= 1000f;
        offsetY %= 1000f;
        offsetZ %= 1000f;
    }

    protected abstract String getId();

    protected static BlockState getBlockStateInstance(String blockStateId) {
        return BlockState.getInstance(blockStateId, EXCEPTION);
    }

    public final void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        randomNoise.setSeed(seed);
        var randseed = (double) randomNoise.noise3D(globalX + offsetX, globalY + offsetY, globalZ + offsetZ);
        random.setSeed(Double.doubleToLongBits(randseed));

        generate(random, zone, globalX, globalY, globalZ);
    }
    protected abstract void generate(Random random, Zone zone, int globalX, int globalY, int globalZ);
}
