package com.arlojay.cosmicearth.worldgen;

import com.github.puzzle.game.events.OnRegisterZoneGenerators;

public class Worldgen {
    public static void register(OnRegisterZoneGenerators registry) {
        registry.registerGenerator(EarthZoneGenerator::new);
    }
}
