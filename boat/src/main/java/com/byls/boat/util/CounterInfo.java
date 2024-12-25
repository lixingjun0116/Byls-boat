package com.byls.boat.util;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

class CounterInfo {
    private AtomicInteger count = new AtomicInteger(0);
    @Getter
    private long lastUpdatedTime;

    public CounterInfo() {
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    public int incrementAndGet() {
        this.lastUpdatedTime = System.currentTimeMillis();
        return this.count.incrementAndGet();
    }

    public void reset() {
        this.count.set(0);
        this.lastUpdatedTime = System.currentTimeMillis();
    }

    public void decrementAndGet() {
        this.count.decrementAndGet();
    }

}