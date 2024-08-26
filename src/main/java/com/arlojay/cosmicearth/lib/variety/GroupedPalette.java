package com.arlojay.cosmicearth.lib.variety;

import java.util.Set;

public class GroupedPalette<ItemType> extends Palette<ItemType> {
    public GroupedPalette(Set<PaletteItem<ItemType>> paletteItems) {
        super(paletteItems);
    }

    public ItemType getItem(double factor) {
        double closest = Double.MAX_VALUE;
        ItemType closestObject = null;
        for(var item : items) {
            var dist = Math.abs(item.factor() - factor);
            if(dist < closest) {
                closest = dist;
                closestObject = item.item();
            }
        }

        return closestObject;
    }
}
