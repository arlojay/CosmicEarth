package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Chunk;

import java.util.ArrayDeque;
import java.util.Queue;

public class NoiseThread {
    private final Queue<NoiseJob> queue = new ArrayDeque<>();
    private final Object addLock = new Object();

    public NoiseThread() {
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

    public NoiseJob createJob3d(NoiseNode noise, Chunk chunk, double[] samples) {
        var job = new NoiseJob(noise, chunk.blockX, chunk.blockY, chunk.blockZ, samples);
        queue.add(job);
        synchronized (addLock) { addLock.notify(); }
        return job;
    }
}
