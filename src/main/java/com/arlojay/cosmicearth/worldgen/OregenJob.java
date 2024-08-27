package com.arlojay.cosmicearth.worldgen;

public class OregenJob extends ThreadJob {
    private final ChunkMask chunkMask;
    private final OreType ore;
    private final int blockOriginX;
    private final int blockOriginY;
    private final int blockOriginZ;
    private final long seed;

    public OregenJob(OreType ore, int blockOriginX, int blockOriginY, int blockOriginZ, long seed, ChunkMask chunkMask) {
        this.ore = ore;
        this.blockOriginX = blockOriginX;
        this.blockOriginY = blockOriginY;
        this.blockOriginZ = blockOriginZ;
        this.seed = seed;
        this.chunkMask = chunkMask;
    }

    public void fulfill() {
        ore.getBlockPositions(chunkMask, seed, blockOriginX, blockOriginY, blockOriginZ);
        super.fulfill();
    }
}