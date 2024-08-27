package com.arlojay.cosmicearth.worldgen;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlockUpdateList implements Iterable<BlockUpdate> {
    public static final RecyclingPool<BlockUpdateList> recyclingPool = new RecyclingPool<>(new RecyclingFactory<>() {
        @Override
        public BlockUpdateList createNew() {
            return new BlockUpdateList();
        }

        @Override
        public void recycle(BlockUpdateList old) {
            for(var oldBlockUpdate : old) oldBlockUpdate.recycle();
            old.blockUpdates.clear();
        }
    });

    private final Set<BlockUpdate> blockUpdates = new HashSet<>();

    public BlockUpdate createNew() {
        var update = BlockUpdate.create();
        this.blockUpdates.add(update);
        return update;
    }

    public void add(BlockUpdate blockUpdate) {
        this.blockUpdates.add(blockUpdate);
    }

    @Override
    public Iterator<BlockUpdate> iterator() {
        return blockUpdates.iterator();
    }

    public static BlockUpdateList create() {
        return recyclingPool.get();
    }

    public void recycle() {
        recyclingPool.recycle(this);
    }
}
