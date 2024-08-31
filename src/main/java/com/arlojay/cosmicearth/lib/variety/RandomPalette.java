package com.arlojay.cosmicearth.lib.variety;

import java.util.Random;
import java.util.Set;

public class RandomPalette<ItemType> extends Palette<ItemType> {
    private final Random random = new Random();

    public RandomPalette(Set<PaletteItem<ItemType>> paletteItems) {
        super(paletteItems);
    }

    public ItemType getItem(double seed) {
        random.setSeed(Double.doubleToLongBits(seed));
        var value = random.nextDouble(0d, totalWeight);

        double aggregate = 0d;
        for(var item : items) {
            aggregate += item.factor();
            if(value < aggregate) return item.item();
        }

        return null;
    }
}
