package com.arlojay.cosmicearth.lib.threading;

public abstract class ThreadJob {
    protected final Object lock = new Object();
    protected boolean finished = false;

    public void waitForFinish() {
        if(finished) return;

        try {
            synchronized (lock) { lock.wait(100); }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void fulfill() {
        finished = true;
        synchronized (lock) { lock.notify(); }
    }
}
