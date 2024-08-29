package com.arlojay.cosmicearth.lib.memory;

public interface RecyclingFactory<T> {
    T createNew();
    default void recycle(T old) {}
}
