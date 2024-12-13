package com.arlojay.cosmicearth;

import com.arlojay.cosmicearth.BlockEvents.BlockEvents;
import com.arlojay.cosmicearth.block.Blocks;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.worldgen.EarthZoneGenerator;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PostModInitializer;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
import com.github.puzzle.game.events.OnRegisterZoneGenerators;
import finalforeach.cosmicreach.blocks.BlockState;
import meteordevelopment.orbit.EventHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

import static finalforeach.cosmicreach.blocks.MissingBlockStateResult.EXCEPTION;

public class CosmicEarthMod implements ModInitializer, PostModInitializer {
    public static final String MOD_ID = "cosmicearth";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInit() {
        PuzzleRegistries.EVENT_BUS.subscribe(this);
        NoiseLoader.registerDefaultNoiseNodes();
    }

    @EventHandler
    public void onEvent(OnRegisterBlockEvent event) {
        Blocks.register(event);
        BlockEvents.register();
    }

    @EventHandler
    public void onEvent(OnRegisterZoneGenerators event) {
        event.registerGenerator(EarthZoneGenerator::new);
    }

    private static void addTagToBlock(BlockState block, String tag) {
        block.tags = block.tags == null ? new String[1] : Arrays.copyOf(block.tags, block.tags.length + 1);
        block.tags[block.tags.length - 1] = tag;
    }

    @Override
    public void onPostInit() {
        addTagToBlock(BlockState.getInstance("base:air[default]", EXCEPTION), "foliage_replaceable");
    }
}
