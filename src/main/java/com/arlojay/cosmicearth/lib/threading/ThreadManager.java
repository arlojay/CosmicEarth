package com.arlojay.cosmicearth.lib.threading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadManager {
    public ThreadExecutor[] noiseThreads;
    private final Map<Object, List<ThreadJob>> jobs = new HashMap<>();
    private int nextIndex;

    public ThreadManager(int count) {
        noiseThreads = new ThreadExecutor[count];
        for(int i = 0; i < noiseThreads.length; i++) {
            noiseThreads[i] = new ThreadExecutor();
        }
    }

    public ThreadExecutor getThread() {
        return noiseThreads[this.nextIndex++ % noiseThreads.length];
    }

    public void addJob(ThreadExecutor thread, Object key, ThreadJob job) {
        var list = jobs.get(key);
        if(list == null) list = new ArrayList<>();

        list.add(job);
        jobs.put(key, list);

        thread.addJob(job);
    }

    public void waitForJobs(Object key) {
        var list = jobs.get(key);
        if(list == null) return;

        for(var job : list) {
            job.waitForFinish();
        }
    }
}
