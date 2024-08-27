package com.arlojay.cosmicearth.worldgen;

public interface RecyclingFactory<T> {
    T createNew();
    default void recycle(T old) {}
}
