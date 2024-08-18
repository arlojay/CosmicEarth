package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.CosmicEarthMod;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.ChunkColumn;
import finalforeach.cosmicreach.worldgen.ZoneGenerator;

public class EarthZoneGenerator extends ZoneGenerator {
    @Override
    public String getSaveKey() {
        return CosmicEarthMod.MOD_ID + ":earth";
    }

    @Override
    public void create() {

    }

    @Override
    public void generateForChunkColumn(Zone zone, ChunkColumn chunkColumn) {

    }

    @Override
    protected String getName() {
        return "Earth";
    }

    @Override
    public int getDefaultRespawnYLevel() {
        return 0;
    }
}
