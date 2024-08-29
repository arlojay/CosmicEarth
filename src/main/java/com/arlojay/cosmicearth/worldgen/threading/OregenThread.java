package com.arlojay.cosmicearth.worldgen.threading;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Chunk;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class OregenThread {
    private final Queue<NoiseJob> queue = new ArrayDeque<>();
    private final Object addLock = new Object();
    private final Map<NoiseNode, NoiseNode> noiseClones = new HashMap<>();

    public OregenThread() {
        var thread = new Thread(() -> {
            try {
                while (true) {
                    if(queue.isEmpty()) {
                        synchronized (addLock) { addLock.wait(1000); }
                    } else {
                        var job = queue.remove();
                        job.fulfill();
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }

    private NoiseNode getThreadNoise(NoiseNode main) {
        if(noiseClones.containsKey(main)) return noiseClones.get(main);

        var copy = main.asCopy();
        noiseClones.put(main, copy);
        return copy;
    }

    public NoiseJob createJob3d(NoiseNode noise, Chunk chunk, double[] samples) {
        var noiseClone = getThreadNoise(noise);
        var job = new NoiseJob(noiseClone, chunk.blockX, chunk.blockY, chunk.blockZ, samples);
        queue.add(job);
        synchronized (addLock) { addLock.notify(); }
        return job;
    }
}
