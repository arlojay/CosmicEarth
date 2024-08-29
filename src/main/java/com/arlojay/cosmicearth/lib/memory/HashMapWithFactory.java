package com.arlojay.cosmicearth.lib.memory;

import java.util.HashMap;
import java.util.function.Supplier;

public class HashMapWithFactory<K, V> extends HashMap<K, V> {
    public V getOrDefault(K key, Supplier<V> defaultValue) {
        var value = super.get(key);
        if(value != null) return value;

        value = defaultValue.get();
        super.put(key, value);
        return value;
    }
}
