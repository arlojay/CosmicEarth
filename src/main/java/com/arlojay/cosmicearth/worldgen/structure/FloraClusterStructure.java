package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.Random;

public class FloraClusterStructure extends WorldgenStructure {
    private final BlockState airBlock = getBlockStateInstance("base:air[default]");
    private final BlockState grassBlock = getBlockStateInstance("base:grass[default]");
    private final BlockState fullGrassBlock = getBlockStateInstance("base:grass[type=full]");
    private final BlockState shortGrassBlock = getBlockStateInstance("cosmicearth:short_grass[default]");
    private final BlockState tallGrassBlock = getBlockStateInstance("cosmicearth:tall_grass[default]");

    private final BlockState[] floorMask = new BlockState[] {
            grassBlock,
            fullGrassBlock
    };
    private final BlockState[] airMask = new BlockState[] {
            airBlock,
            shortGrassBlock,
            tallGrassBlock
    };
    private final BlockState[] flowers = new BlockState[] {
            getBlockStateInstance("cosmicearth:black_iris[default]"),
            getBlockStateInstance("cosmicearth:black_tulip[default]"),
            getBlockStateInstance("cosmicearth:bluebell[default]"),
            getBlockStateInstance("cosmicearth:buttercup[default]"),
            getBlockStateInstance("cosmicearth:daisy[default]"),
            getBlockStateInstance("cosmicearth:fire_iris[default]"),
            getBlockStateInstance("cosmicearth:hyacinth[default]"),
            getBlockStateInstance("cosmicearth:narcissus[default]"),
            getBlockStateInstance("cosmicearth:orange_pansy[default]"),
            getBlockStateInstance("cosmicearth:orange_tulip[default]"),
            getBlockStateInstance("cosmicearth:orchid[default]"),
            getBlockStateInstance("cosmicearth:pink_lily[default]"),
            getBlockStateInstance("cosmicearth:pink_mimosa[default]"),
            getBlockStateInstance("cosmicearth:pink_tulip[default]"),
            getBlockStateInstance("cosmicearth:purple_iris[default]"),
            getBlockStateInstance("cosmicearth:purple_pansy[default]"),
            getBlockStateInstance("cosmicearth:red_pansy[default]"),
            getBlockStateInstance("cosmicearth:red_tulip[default]"),
            getBlockStateInstance("cosmicearth:rose[default]"),
            getBlockStateInstance("cosmicearth:violet[default]"),
            getBlockStateInstance("cosmicearth:white_iris[default]"),
            getBlockStateInstance("cosmicearth:white_lily[default]"),
            getBlockStateInstance("cosmicearth:white_mimosa[default]"),
            getBlockStateInstance("cosmicearth:white_pansy[default]"),
            getBlockStateInstance("cosmicearth:yellow_lily[default]"),
            getBlockStateInstance("cosmicearth:yellow_tulip[default]"),
            getBlockStateInstance("cosmicearth:shrub[default]"),
    };
    private final WhiteNoise randomNoise = new WhiteNoise();

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        var random = new Random(Double.doubleToLongBits(randomNoise.noise3D(globalX, globalY, globalZ) * Double.MAX_VALUE));

        var blockState = flowers[random.nextInt(0, flowers.length)];
        double clusterSize = random.nextDouble(2d, 6d);

        BuildHelper.scatterBlock(
                zone, random.nextLong(),
                bs -> bs.hasTag("soil_tropical"),
                bs -> bs.hasTag("foliage_replaceable"),
                globalX, globalY, globalZ,
                clusterSize, 0.1d,
                blockState
        );
    }
}
