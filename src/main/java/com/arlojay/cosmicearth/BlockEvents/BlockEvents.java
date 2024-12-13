package com.arlojay.cosmicearth.BlockEvents;

import com.arlojay.cosmicearth.CosmicEarthMod;
import finalforeach.cosmicreach.util.Identifier;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.github.puzzle.game.resources.PuzzleGameAssetLoader.locateAsset;
import static finalforeach.cosmicreach.blockevents.BlockEvents.loadBlockEventsFromAsset;

public class BlockEvents {
    private static final Set<String> registeredBlocks = new HashSet<>();

    public static void register() {
        for(var id : registeredBlocks) {
            var file = "block_events/" + id + ".json";
            loadBlockEventsFromAsset(Objects.requireNonNull(locateAsset(Identifier.of(CosmicEarthMod.MOD_ID, file))));
        }
    }

    private static void register(String id) {
        registeredBlocks.add(id);
    }

    static {
        register("block_events_no_drop");
    }
}
