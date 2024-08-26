package com.arlojay.cosmicearth.worldgen;

import java.util.ArrayList;
import java.util.function.Supplier;

public class RecyclingPool<T> {
    private final ArrayList<T> freeItems;
    private final Supplier<T> factory;

    public RecyclingPool(Supplier<T> factory) {
        this.freeItems = new ArrayList<>();
        this.factory = factory;
    }

    public T get() {
        if(freeItems.size() > 0) return freeItems.remove(0);

        return factory.get();
    }

    public void recycle(T item) {
        freeItems.add(item);
    }
}
