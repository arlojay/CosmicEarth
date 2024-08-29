package com.arlojay.cosmicearth.worldgen;


import com.arlojay.cosmicearth.lib.spline.Interpolator;
import com.arlojay.cosmicearth.lib.spline.SplineInterpolator;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.List;
import java.util.Random;

public class OreType {
    private final int maxSize;
    private final BlockState block;
    private final int minPerChunk;
    private final int maxPerChunk;
    private final int minSize;
    private final SplineInterpolator<?> densityMap;
    private final List<BlockState> replaceMask;
    private final WhiteNoise orePositionSeedGenerator = new WhiteNoise();

    public OreType(
            BlockState block,
            int minPerChunk, int maxPerChunk,
            int minSize, int maxSize,
            SplineInterpolator<?> densityMap,
            List<BlockState> replaceMask
    ) {
        this.block = block;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.densityMap = densityMap;
        this.replaceMask = replaceMask;
    }

    public int getChunkCount(Random random) {
        return random.nextInt(minPerChunk, maxPerChunk + 1);
    }

    public int getSize(Random random) {
        return random.nextInt(minSize, maxSize + 1);
    }

    public BlockState getBlock() {
        return block;
    }

    public void place(ChunkMask mask, Random random, int x, int y, int z) {
        var size = getSize(random);
        OreGenerator.generateChunkMask(
                mask, x, y, z, size,
                random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
        );
    }

    public ChunkMask getBlockPositions(ChunkMask chunkMask, long seed, int startX, int startY, int startZ) {
        orePositionSeedGenerator.setSeed(seed);
        var random = new Random(Float.floatToIntBits(
                // haha funny numbers
                orePositionSeedGenerator.noise3D(
                        startX - 328,
                        startY + 420,
                        startZ + 69
                )
        ));

        int count = getChunkCount(random);

        for(int i = 0; i < count; i++) {
            int oreX = startX + random.nextInt(0, 16);
            int oreY = startY + random.nextInt(0, 16);
            int oreZ = startZ + random.nextInt(0, 16);

            double rolledDensity = random.nextDouble();
            if(rolledDensity > densityMap.interpolate(oreY)) continue;

            this.place(chunkMask, random, oreX, oreY, oreZ);
        }

        return chunkMask;
    }

    public List<BlockState> getReplaceMask() {
        return replaceMask;
    }
}
