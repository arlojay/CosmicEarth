package com.arlojay.cosmicearth.worldgen;

import java.util.HashMap;
import java.util.Map;

public class MapOfCopyables<T extends Copyable<T>> {
    private final Map<T, T> copies = new HashMap<>();

    public T get(T original) {
        if(copies.containsKey(original)) return copies.get(original);

        var copy = original.asCopy();
        copies.put(original, copy);
        return copy;
    }
}
