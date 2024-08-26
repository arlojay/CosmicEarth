package com.arlojay.cosmicearth.worldgen;


import com.arlojay.cosmicearth.lib.spline.SplineMapper;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.List;
import java.util.Random;

public class OreType {
    private final int maxSize;
    private final BlockState block;
    private final int minPerChunk;
    private final int maxPerChunk;
    private final int minSize;
    private final SplineMapper densityMap;
    private final List<BlockState> replaceMask;
    private final WhiteNoise orePositionSeedGenerator = new WhiteNoise();

    public OreType(
            BlockState block,
            int minPerChunk, int maxPerChunk,
            int minSize, int maxSize,
            SplineMapper densityMap,
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

    public void place(Zone zone, Random random, int x, int y, int z) {
        var size = getSize(random);
        OreGenerator.generateOreVein(
                zone, block, replaceMask, x, y, z, size,
                random.nextLong(Long.MIN_VALUE, Long.MAX_VALUE)
        );
    }

    public void populateChunk(Zone zone, Chunk chunk, long seed) {
        int globalX = chunk.blockX;
        int globalY = chunk.blockY;
        int globalZ = chunk.blockZ;

        orePositionSeedGenerator.setSeed(seed);
        var random = new Random(Float.floatToIntBits(
                // haha funny numbers
                orePositionSeedGenerator.noise3D(
                        globalX - 328,
                        globalY + 420,
                        globalZ + 69
                )
        ));

        int count = getChunkCount(random);

        for(int i = 0; i < count; i++) {
            int oreX = globalX + random.nextInt(0, 16);
            int oreY = globalY + random.nextInt(0, 16);
            int oreZ = globalZ + random.nextInt(0, 16);

            double rolledDensity = random.nextDouble();
            if(rolledDensity > densityMap.transform(oreY)) continue;

            this.place(zone, random, oreX, oreY, oreZ);
        }
    }
}
