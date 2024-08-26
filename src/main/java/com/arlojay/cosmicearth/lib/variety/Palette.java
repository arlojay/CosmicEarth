package com.arlojay.cosmicearth.lib.variety;

import java.util.HashSet;
import java.util.Set;

public abstract class Palette<ItemType> {
    protected final Set<PaletteItem<ItemType>> items;
    protected double totalWeight = 0d;

    public Palette() {
        this(new HashSet<>());
    }
    public Palette(Set<PaletteItem<ItemType>> items) {
        this.items = new HashSet<>();
        this.items.addAll(items);
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
            totalWeight += item.factor();
        }
    }

    public abstract ItemType getItem(double factor);
}
