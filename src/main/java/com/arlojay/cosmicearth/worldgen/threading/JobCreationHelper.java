package com.arlojay.cosmicearth.worldgen.threading;

import com.arlojay.cosmicearth.lib.memory.HashMapWithFactory;
import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import com.arlojay.cosmicearth.lib.threading.MapOfCopyables;
import com.arlojay.cosmicearth.lib.threading.ThreadExecutor;
import com.arlojay.cosmicearth.worldgen.mask.ChunkMask;
import com.arlojay.cosmicearth.worldgen.ore.OreType;
import finalforeach.cosmicreach.world.Chunk;

public class JobCreationHelper {
    private static final HashMapWithFactory<ThreadExecutor, MapOfCopyables<NoiseNode>> noiseCopies = new HashMapWithFactory<>();
    public static NoiseJob createNoiseJob(NoiseNode node, ThreadExecutor thread, Chunk chunk, double[] samples) {
        var copies = noiseCopies.getOrDefault(thread, MapOfCopyables::new);

        return new NoiseJob(copies.get(node), chunk.blockX, chunk.blockY, chunk.blockZ, samples);
    }
    public static OregenJob createOregenJob(Chunk chunk, OreType ore, long seed, ChunkMask chunkMask) {
        return new OregenJob(ore, chunk.blockX, chunk.blockY, chunk.blockZ, seed, chunkMask);
    }
}
