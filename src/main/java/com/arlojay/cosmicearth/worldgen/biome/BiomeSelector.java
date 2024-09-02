package com.arlojay.cosmicearth.worldgen.biome;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class BiomeSelector {
    private final List<Biome> biomes = new ArrayList<>();

    private final ThreadLocal<List<Biome>> predicates = ThreadLocal.withInitial(ArrayList::new);
    private final ThreadLocal<List<Biome>> predicates2 = ThreadLocal.withInitial(ArrayList::new);

    public Biome getBiome(double temperature, double humidity, double erosion, double continent) {
        predicates.get().clear();

        double biomeTemperature;
        double biomeHumidity;
        double biomeErosion;
        double biomeContinent;

        double dTemperature;
        double dHumidity;
        double dErosion;
        double dContinent;

        double distance;
        double closest = Double.MAX_VALUE;
        Biome closestBiome = null;

        for(var biome : biomes) if(
                biome.temperature.isWithin(temperature) &&
                biome.humidity.isWithin(humidity) &&
                biome.erosion.isWithin(erosion) &&
                biome.continent.isWithin(continent)
        ) predicates.get().add(biome);

        // If no biomes meet criteria, find nearest fit biome
        if(predicates.get().isEmpty()) {
            int matching;
            int highestMatching = -1;
            predicates2.get().clear();

            for(var predicate : biomes) {
                matching = 0;

                if(predicate.temperature.isWithin(temperature)) matching++;
                if(predicate.humidity.isWithin(humidity)) matching++;
                if(predicate.erosion.isWithin(erosion)) matching++;
                if(predicate.continent.isWithin(continent)) matching += 5;

                if(matching > highestMatching) {
                    predicates2.get().clear();
                    highestMatching = matching;
                }
                if(highestMatching == matching) {
                    predicates2.get().add(predicate);
                }
            }

            // If only one biome matches the most criteria, return that
            if(predicates2.get().size() == 1) return predicates2.get().get(0);

            // If there are multiple biomes that meet the same amount of criteria, find the one closest to the passed values
            for(var biome : predicates2.get()) {
                biomeTemperature = MathUtils.clamp(temperature, biome.temperature.min, biome.temperature.max);
                biomeHumidity = MathUtils.clamp(humidity, biome.humidity.min, biome.humidity.max);
                biomeErosion = MathUtils.clamp(erosion, biome.erosion.min, biome.erosion.max);
                biomeContinent = MathUtils.clamp(continent, biome.continent.min, biome.continent.max);

                dTemperature = temperature - biomeTemperature;
                dHumidity = humidity - biomeHumidity;
                dErosion = erosion - biomeErosion;
                dContinent = continent - biomeContinent;

                distance = dTemperature * dTemperature + dHumidity * dHumidity + dErosion * dErosion + dContinent * dContinent;

                if(distance < closest) {
                    closest = distance;
                    closestBiome = biome;
                }
            }

            return closestBiome;
        }

        // If one biome meets all criteria, return that
        if(predicates.get().size() == 1) return predicates.get().get(0);

        // If more than biome matches all criteria, find the one with middle values closest to the passed values
        for(var biome : predicates.get()) {
            biomeTemperature = biome.temperature.getMiddle();
            biomeHumidity = biome.humidity.getMiddle();
            biomeErosion = biome.erosion.getMiddle();
            biomeContinent = biome.continent.getMiddle();

            dTemperature = temperature - biomeTemperature;
            dHumidity = humidity - biomeHumidity;
            dErosion = erosion - biomeErosion;
            dContinent = continent - biomeContinent;

            distance = dTemperature * dTemperature + dHumidity * dHumidity + dErosion * dErosion + dContinent * dContinent;

            if(distance < closest) {
                closest = distance;
                closestBiome = biome;
            }
        }

        return closestBiome;
    }

    public void registerBiome(Biome biome) {
        biomes.add(biome);
    }
}
