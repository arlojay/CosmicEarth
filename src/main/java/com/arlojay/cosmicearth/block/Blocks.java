package com.arlojay.cosmicearth.block;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.events.OnRegisterBlockEvent;

import java.util.HashSet;
import java.util.Set;

public class Blocks {
    private static final Set<String> registeredBlocks = new HashSet<>();

    public static void register(OnRegisterBlockEvent registry) {
        for(var id : registeredBlocks) {
            var path = "blocks/" + id + ".json";
            registry.registerBlock(() -> new DataModBlock(id, new ResourceLocation(CosmicEarthMod.MOD_ID, path)));
        }
    }

    private static void register(String id) {
        registeredBlocks.add(id);
    }


    static {
        register("black_iris");
        register("black_tulip");
        register("bluebell");
        register("buttercup");
        register("cactus");
        register("cactus_flower");
        register("coniferous_leaves");
        register("daisy");
//        register("dead_grass");
        register("deciduous_leaves");
        register("fire_iris");
        register("hyacinth");
        register("narcissus");
        register("orange_pansy");
        register("orange_tulip");
        register("orchid");
        register("pebbles");
        register("pink_lily");
        register("pink_mimosa");
        register("pink_tulip");
        register("purple_iris");
        register("purple_pansy");
        register("red_pansy");
        register("red_tulip");
        register("rose");
        register("short_grass");
        register("shrub");
        register("tall_grass");
        register("violet");
        register("white_iris");
        register("white_lily");
        register("white_mimosa");
        register("white_pansy");
        register("yellow_lily");
        register("yellow_tulip");
    }
}
