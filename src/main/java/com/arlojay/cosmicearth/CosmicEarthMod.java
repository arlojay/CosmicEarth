package com.arlojay.cosmicearth;

import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
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

        LOGGER.info("Hello From INIT");
    }

    @Subscribe
    public void onEvent(OnRegisterBlockEvent event) {

    }
}
