package com.arlojay.cosmicearth.worldgen;

import com.arlojay.cosmicearth.lib.noise.NoiseNode;
import finalforeach.cosmicreach.world.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoiseThreadColumn {
    // TODO: fix multiple noise threads messing up cave generation
    public NoiseThread[] noiseThreads = new NoiseThread[1];
    private final Map<Chunk, List<NoiseJob>> jobs = new HashMap<>();
    private int nextIndex;

    public NoiseThreadColumn() {
        for(int i = 0; i < noiseThreads.length; i++) {
            noiseThreads[i] = new NoiseThread();
        }
    }

    private NoiseThread getNextThread() {
        return noiseThreads[this.nextIndex++ % noiseThreads.length];
    }

    public void process(Chunk chunk, NoiseNode noise, double[] samples) {
        var job = getNextThread().createJob3d(noise, chunk, samples);

        var list = jobs.getOrDefault(chunk, new ArrayList<>());
        list.add(job);
        jobs.put(chunk, list);
    }

    public void waitForJobs(Chunk chunk) {
        var list = jobs.get(chunk);
        if(list == null) return;

        for(var job : list) {
            job.waitForFinish();
        }
    }
}
