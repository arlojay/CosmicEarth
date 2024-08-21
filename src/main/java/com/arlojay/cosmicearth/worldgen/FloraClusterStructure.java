package com.arlojay.cosmicearth.worldgen;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

public class FloraClusterStructure extends WorldgenStructure {
    private final BlockState airBlock = getBlockStateInstance("base:air[default]");
    private final BlockState grassBlock = getBlockStateInstance("base:grass[default]");
    private final BlockState fullGrassBlock = getBlockStateInstance("base:grass[type=full]");

    private final BlockState[] floorMask = new BlockState[] {
            grassBlock,
            fullGrassBlock
    };
    private final BlockState[] airMask = new BlockState[] {
            airBlock
    };
    private final BlockState[] flowers = new BlockState[] {
            getBlockStateInstance("cosmicearth:black_iris[default]"),
            getBlockStateInstance("cosmicearth:black_tulip[default]"),
            getBlockStateInstance("cosmicearth:bluebell[default]"),
            getBlockStateInstance("cosmicearth:buttercup[default]"),
            getBlockStateInstance("cosmicearth:cactus[default]"),
            getBlockStateInstance("cosmicearth:cactus_flower[default]"),
            getBlockStateInstance("cosmicearth:coniferous_leaves[default]"),
            getBlockStateInstance("cosmicearth:daisy[default]"),
            getBlockStateInstance("cosmicearth:dead_grass[default]"),
            getBlockStateInstance("cosmicearth:deciduous_leaves[default]"),
            getBlockStateInstance("cosmicearth:fire_iris[default]"),
            getBlockStateInstance("cosmicearth:hyacinth[default]"),
            getBlockStateInstance("cosmicearth:narcissus[default]"),
            getBlockStateInstance("cosmicearth:orange_pansy[default]"),
            getBlockStateInstance("cosmicearth:orange_tulip[default]"),
            getBlockStateInstance("cosmicearth:orchid[default]"),
            getBlockStateInstance("cosmicearth:pebbles[default]"),
            getBlockStateInstance("cosmicearth:pink_lily[default]"),
            getBlockStateInstance("cosmicearth:pink_mimosa[default]"),
            getBlockStateInstance("cosmicearth:pink_tulip[default]"),
            getBlockStateInstance("cosmicearth:purple_iris[default]"),
            getBlockStateInstance("cosmicearth:purple_pansy[default]"),
            getBlockStateInstance("cosmicearth:red_pansy[default]"),
            getBlockStateInstance("cosmicearth:red_tulip[default]"),
            getBlockStateInstance("cosmicearth:rose[default]"),
            getBlockStateInstance("cosmicearth:short_grass[default]"),
            getBlockStateInstance("cosmicearth:shrub[default]"),
            getBlockStateInstance("cosmicearth:tall_grass[default]"),
            getBlockStateInstance("cosmicearth:violet[default]"),
            getBlockStateInstance("cosmicearth:white_iris[default]"),
            getBlockStateInstance("cosmicearth:white_lily[default]"),
            getBlockStateInstance("cosmicearth:white_mimosa[default]"),
            getBlockStateInstance("cosmicearth:white_pansy[default]"),
            getBlockStateInstance("cosmicearth:yellow_lily[default]"),
            getBlockStateInstance("cosmicearth:yellow_tulip[default]")
    };
    private final WhiteNoise flowerTypeNoise = new WhiteNoise();
    private final WhiteNoise clusterSizeNoise = new WhiteNoise();
    private final WhiteNoise clusterDensityNoise = new WhiteNoise();

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        flowerTypeNoise.setSeed(seed);
        clusterSizeNoise.setSeed(seed + 1);
        clusterDensityNoise.setSeed(seed + 2);

        int floraIndex = (int) (Math.floor(flowerTypeNoise.noise3D(globalX, globalY, globalZ) * 0.5f + 0.5f) * (float) flowers.length);
        var blockState = flowers[floraIndex];
        double clusterSize = (clusterSizeNoise.noise3D(globalX, globalY, globalZ) * 0.5f + 0.5f) * 4d + 2d;

        BuildHelper.scatterBlock(
                zone,
                floorMask, airMask,
                globalX, globalY, globalZ,
                clusterSize, 0.3d,
                blockState,
                clusterDensityNoise
        );
    }
}
