package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Chunk;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class ThreadExecutor {
    private final Queue<ThreadJob> queue = new ArrayDeque<>();
    private final Object addLock = new Object();

    public ThreadExecutor() {
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

    protected void addJob(ThreadJob job) {
        queue.add(job);
        synchronized (addLock) { addLock.notify(); }
    }
}
