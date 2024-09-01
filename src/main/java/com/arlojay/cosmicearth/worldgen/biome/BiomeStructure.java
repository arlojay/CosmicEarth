package com.arlojay.cosmicearth.worldgen.biome;

import com.arlojay.cosmicearth.lib.Range;
import com.arlojay.cosmicearth.worldgen.structure.WorldgenStructure;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;

public abstract class BiomeStructure {
    public final double averageDistance;
    public final double chance;
    public final WorldgenStructure worldgenStructure;
    public final Range gradientRange;

    public BiomeStructure() {
        this.averageDistance = getAverageDistance();
        this.chance = 1d / (averageDistance * averageDistance);

        this.worldgenStructure = getWorldgenStructure();

        this.gradientRange = getGradientRange();
    }

    protected abstract double getAverageDistance();
    protected abstract WorldgenStructure getWorldgenStructure();

    protected Range getGradientRange() {
        return new Range(0d, Double.MAX_VALUE);
    }

    public abstract boolean canSpawn(Zone zone, int blockX, int blockY, int blockZ, BlockState ground, BlockState air);
}
