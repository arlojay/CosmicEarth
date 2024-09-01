package com.arlojay.cosmicearth.worldgen.structure;

import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.Random;
import java.util.function.Function;

public abstract class ClusterStructure extends WorldgenStructure {
    private final WhiteNoise randomNoise = new WhiteNoise();
    private final BlockState blockType;
    private final double maxRadius;
    private final double minRadius;
    public final Function<BlockState, Boolean> groundChecker;
    public final Function<BlockState, Boolean> airChecker;
    private final double density;

    public ClusterStructure(BlockState blockType, double minRadius, double maxRadius, double density) {
        this.blockType = blockType;
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.groundChecker = createGroundChecker();
        this.airChecker = createAirChecker();
        this.density = density;
    }

    public abstract Function<BlockState, Boolean> createGroundChecker();
    public abstract Function<BlockState, Boolean> createAirChecker();

    @Override
    public void generate(long seed, Zone zone, int globalX, int globalY, int globalZ) {
        randomNoise.setSeed(seed);
        var random = new Random(Double.doubleToLongBits(randomNoise.noise3D(globalX, globalY, globalZ) * Double.MAX_VALUE));

        double clusterSize = random.nextDouble(minRadius, maxRadius);

        BuildHelper.scatterBlock(
                zone, random.nextLong(),
                groundChecker,
                airChecker,
                globalX, globalY, globalZ,
                clusterSize, density,
                blockType
        );
    }
}
