package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Chunk;

public class JobCreationHelpler {
    private static final HashMapWithFactory<ThreadExecutor, MapOfCopyables<NoiseNode>> noiseCopies = new HashMapWithFactory<>();
    public static NoiseJob createNoiseJob(NoiseNode node, ThreadExecutor thread, Chunk chunk, double[] samples) {
        var copies = noiseCopies.getOrDefault(thread, MapOfCopyables::new);

        return new NoiseJob(copies.get(node), chunk.blockX, chunk.blockY, chunk.blockZ, samples);
    }
    public static OregenJob createOregenJob(Chunk chunk, OreType ore, long seed, ChunkMask chunkMask) {
        return new OregenJob(ore, chunk.blockX, chunk.blockY, chunk.blockZ, seed, chunkMask);
    }
}
