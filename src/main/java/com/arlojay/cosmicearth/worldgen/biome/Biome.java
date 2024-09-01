package com.arlojay.cosmicearth.worldgen.biome;

import com.arlojay.cosmicearth.lib.Range;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.world.Zone;
import finalforeach.cosmicreach.worldgen.noise.WhiteNoise;

import java.util.*;

public abstract class Biome {
    protected Range temperature;
    protected Range humidity;
    protected Range erosion;
    protected Range continent;

    private final BiomeStructure[] structures;
    protected final BlockGenerator topsoilGenerator;
    protected final BlockGenerator loamGenerator;

    private final WhiteNoise structureNoise = new WhiteNoise();
    private final Random random = new Random();
    private final long seed;

    public Biome(long seed, Range temperature, Range humidity, Range erosion, Range continent) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.erosion = erosion;
        this.continent = continent;
        this.structures = Arrays.stream(this.getStructureArray()).sorted(Comparator.comparingDouble(r -> r.chance)).toArray(BiomeStructure[]::new);

        this.topsoilGenerator = getTopsoilGenerator(seed);
        this.loamGenerator = getLoamGenerator(seed);

        this.seed = seed;
        structureNoise.setSeed(seed);
    }

    public abstract String getName();

    private BiomeStructure[] getStructureArray() {
        var list = new ArrayList<BiomeStructure>();
        addStructures(list);
        return list.toArray(BiomeStructure[]::new);
    }

    protected abstract void addStructures(List<BiomeStructure> structures);
    protected abstract BlockGenerator getTopsoilGenerator(long seed);
    protected abstract BlockGenerator getLoamGenerator(long seed);

    public final BlockState getTopsoil(int x, int y, int z, double gradient) {
        return topsoilGenerator.getBlock(x, y, z, gradient);
    }
    public final BlockState getLoam(int x, int y, int z, double gradient) {
        return loamGenerator.getBlock(x, y, z, gradient);
    }

    public final BiomeStructure getStructure(Zone zone, int x, int y, int z, BlockState ground, BlockState air, double gradient) {
        structureNoise.setSeed(seed + Double.doubleToLongBits((x + 0.581d) * (y + 0.953d) * (z + 1.284d)));
        random.setSeed(Double.doubleToLongBits(structureNoise.noise3D((float) x + 8428f, (float) y + 1830f, (float) z + 5837f)));

        for(var structure : structures) {
            double factor = random.nextDouble();

            if(!structure.gradientRange.isWithin(gradient)) continue;

            if(factor < structure.chance && structure.canSpawn(zone, x, y, z, ground, air)) return structure;
        }

        return null;
    }
}
