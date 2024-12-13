package com.arlojay.cosmicearth;

import com.arlojay.cosmicearth.worldgen.EarthZoneGenerator;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import finalforeach.cosmicreach.ui.debug.DebugItem;
import finalforeach.cosmicreach.ui.debug.DebugStringItem;

import java.util.HashMap;
import java.util.Map;

public class Debug {
    private static final Map<String, DebugItem> debugItems = new HashMap<>();

    public static void addDebugItem(String id, DebugItem item) {
        if(debugItems.containsKey(id)) DebugInfo.removeDebugItem(debugItems.get(id));
        debugItems.put(id, item);
        DebugInfo.addDebugItem(item);
    }

    public static DebugItem biomeNoiseDebug = new DebugStringItem(true, () -> {
        var localPlayer = InGame.getLocalPlayer();
        var position = localPlayer.getPosition();

        double temperature = EarthZoneGenerator.temperatureNoise.sample(position.x, position.z);
        double humidity = EarthZoneGenerator.humidityNoise.sample(position.x, position.z);
        double erosion = EarthZoneGenerator.erosionBaseNoise.sample(position.x, position.z);
        double continentalness = EarthZoneGenerator.continentalnessNoise.sample(position.x, position.z);
        double weirdness = EarthZoneGenerator.weirdnessNoise.sample(position.x, position.z);
        double peaks = EarthZoneGenerator.peaksNoise.sample(position.x, position.z);

        var biome = EarthZoneGenerator.biomeSelector.getBiome(temperature, humidity, erosion, continentalness);

        return "T: " + (Math.round(temperature * 1000d) / 1000d) + " " +
                "H: " + (Math.round(humidity * 1000d) / 1000d) + " " +
                "E: " + (Math.round(erosion * 1000d) / 1000d) + " " +
                "C: " + (Math.round(continentalness * 1000d) / 1000d) + " " +
                "W: " + (Math.round(weirdness * 1000d) / 1000d) +  " " +
                "P: " + (Math.round(peaks * 1000d) / 1000d) +  " " +
                "\nBiome: " + biome.getName();
    }, l -> l);
}
