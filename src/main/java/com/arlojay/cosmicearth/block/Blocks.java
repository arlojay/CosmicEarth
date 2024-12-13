package com.arlojay.cosmicearth.block;

import com.arlojay.cosmicearth.CosmicEarthMod;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
import finalforeach.cosmicreach.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class Blocks {
    private static class BlockProperties {
        protected double breakSpeed;

        protected BlockProperties() {
            this.breakSpeed = 1.0d;
        }

        protected BlockProperties breakSpeed(double speed) {
            this.breakSpeed = speed;
            return this;
        }
    }
    private static final Set<String> registeredBlocks = new HashSet<>();

    public static void register(OnRegisterBlockEvent registry) {
        for(var id : registeredBlocks) {
            var file = id + ".json";
            registry.registerBlock(() -> new DataModBlock(Identifier.of(CosmicEarthMod.MOD_ID, file)));
        }
    }

    private static void register(String id, BlockProperties properties) {
        registeredBlocks.add(id);
    }

    private static BlockProperties plant() {
        return new BlockProperties();
    }

    private static BlockProperties flower() {
        return plant();
    }

    private static BlockProperties leaves() {
        return new BlockProperties();
    }

    static {
        register("cactus", new BlockProperties());
        register("coniferous_leaves", leaves());
        register("deciduous_leaves", leaves());
//        register("dead_grass");

        register("pebbles", new BlockProperties());

        register("tall_grass", plant());
        register("short_grass", plant());
        register("shrub", plant());

        register("black_iris", flower());
        register("black_tulip", flower());
        register("bluebell", flower());
        register("buttercup", flower());
        register("cactus_flower", flower());
        register("daisy", flower());
        register("fire_iris", flower());
        register("hyacinth", flower());
        register("narcissus", flower());
        register("orange_pansy", flower());
        register("orange_tulip", flower());
        register("orchid", flower());
        register("pink_lily", plant());
        register("pink_mimosa", plant());
        register("pink_tulip", plant());
        register("purple_iris", plant());
        register("purple_pansy", plant());
        register("red_pansy", plant());
        register("red_tulip", plant());
        register("rose", plant());
        register("violet", plant());
        register("white_iris", plant());
        register("white_lily", plant());
        register("white_mimosa", plant());
        register("white_pansy", plant());
        register("yellow_lily", plant());
        register("yellow_tulip", plant());
    }
}
