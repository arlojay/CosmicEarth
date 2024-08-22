package com.arlojay.cosmicearth.lib.variety;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Palette<ItemType> {
    private final Set<PaletteItem<ItemType>> items;
    private final Random random = new Random();
    private double totalWeight = 0d;

    public Palette() {
        this(new HashSet<>());
    }
    public Palette(HashSet<PaletteItem<ItemType>> items) {
        this.items = new HashSet<>();
        this.items.addAll(items);
    }

    @SafeVarargs
    public Palette(PaletteItem<ItemType> ...items) {
        this.items = new HashSet<>();

        for(PaletteItem<ItemType> paletteItem : items) {
            if(!(paletteItem instanceof PaletteItem<ItemType>)) continue;
            this.items.add(paletteItem);
        }
        rebuildCache();
    }

    public PaletteItem<ItemType> addItem(ItemType item, double weight) {
        var paletteItem = new PaletteItem<>(item, weight);
        this.items.add(paletteItem);
        rebuildCache();
        return paletteItem;
    }

    public void addItem(PaletteItem<ItemType> paletteItem) {
        this.items.add(paletteItem);
        rebuildCache();
    }

    public void removeItem(PaletteItem<ItemType> paletteItem) {
        this.items.remove(paletteItem);
        rebuildCache();
    }

    private void rebuildCache() {
        this.totalWeight = 0d;
        for(var item : items) {
            totalWeight += item.weight();
        }
    }

    public ItemType getRandomItem(long seed) {
        random.setSeed(seed);
        var value = random.nextDouble(0d, totalWeight);

        double aggregate = 0d;
        for(var item : items) {
            aggregate += item.weight();
            if(value < aggregate) return item.item();
        }

        return null;
    }
}
