package com.arlojay.cosmicearth.worldgen.mask;

import com.arlojay.cosmicearth.lib.memory.RecyclingPool;
import finalforeach.cosmicreach.blocks.BlockState;

public class BlockUpdate {
    public static final RecyclingPool<BlockUpdate> recyclingPool = new RecyclingPool<>(BlockUpdate::new);

    protected BlockState block;
    protected int blockX;
    protected int blockY;
    protected int blockZ;


    public static BlockUpdate create() {
        return recyclingPool.get();
    }

    public void recycle() {
        recyclingPool.recycle(this);
    }
}
