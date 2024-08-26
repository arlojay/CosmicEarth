package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;

import static finalforeach.cosmicreach.world.Chunk.CHUNK_WIDTH;

public class NoiseChunkSampler {
    public static double[] sample(NoiseNode noise, double[] samples, int start) {
        for(int t = 0; t < CHUNK_WIDTH; t++) {
            samples[t] = noise.sample(start + t);
        }

        return samples;
    }

    public static double[] sample(NoiseNode noise, double[] samples, int startX, int startY) {
        int i = 0;
        for(int x = 0; x < CHUNK_WIDTH; x++) {
            for(int y = 0; y < CHUNK_WIDTH; y++) {
                samples[i++] = noise.sample(startX + x, startY + y);
            }
        }

        return samples;
    }

    public static double[] sample(NoiseNode noise, double[] samples, int startX, int startY, int startZ) {
        int i = 0;
        for(int y = 0; y < CHUNK_WIDTH; y++) {
            for(int x = 0; x < CHUNK_WIDTH; x++) {
                for(int z = 0; z < CHUNK_WIDTH; z++) {
                    samples[i++] = noise.sample(startX + x, startY + y, startZ + z);
                }
            }
        }

        return samples;
    }

    public static double[] sample(NoiseNode noise, double[] samples, int startX, int startY, int startZ, int startW) {
        int i = 0;

        for(int x = 0; x < CHUNK_WIDTH; x++) {
            for(int y = 0; y < CHUNK_WIDTH; y++) {
                for(int z = 0; z < CHUNK_WIDTH; z++) {
                    for(int w = 0; w < CHUNK_WIDTH; w++) {
                        samples[i++] = noise.sample(startX + x, startY + y, startZ + z, startW + w);
                    }
                }
            }
        }

        return samples;
    }
}
