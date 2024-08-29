package com.arlojay.cosmicearth;

import com.arlojay.cosmicearth.block.Blocks;
import com.arlojay.cosmicearth.lib.noise.loader.NoiseLoader;
import com.arlojay.cosmicearth.worldgen.EarthZoneGenerator;
import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
import com.github.puzzle.game.events.OnRegisterZoneGenerators;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.greenrobot.eventbus.Subscribe;

public class CosmicEarthMod implements ModInitializer {
    public static final String MOD_ID = "cosmicearth";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInit() {
        PuzzleRegistries.EVENT_BUS.register(this);
        NoiseLoader.registerDefaultNoiseNodes();
    }

    @Subscribe
    public void onEvent(OnRegisterBlockEvent event) {
        Blocks.register(event);
    }

    @Subscribe
    public void onEvent(OnRegisterZoneGenerators event) {
        event.registerGenerator(EarthZoneGenerator::new);
    }
}
