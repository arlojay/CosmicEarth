package com.arlojay.cosmicearth.lib.memory;

import java.util.ArrayList;

public class RecyclingPool<T> {
    private final ArrayList<T> freeItems;
    private final RecyclingFactory<T> factory;

    public RecyclingPool(RecyclingFactory<T> factory) {
        this.freeItems = new ArrayList<>();
        this.factory = factory;
    }

    public T get() {
        if(freeItems.size() > 0) {
            var old = freeItems.remove(0);
            factory.recycle(old);
            return old;
        }

        return factory.createNew();
    }

    public void recycle(T item) {
        freeItems.add(item);
    }
}
