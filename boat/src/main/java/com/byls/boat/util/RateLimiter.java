package com.byls.boat.util;

import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {
    private final ConcurrentHashMap<String, CounterInfo> counters = new ConcurrentHashMap<>();
    // 时间窗口，单位为毫秒
    private final long timeWindowMillis;

    public RateLimiter(long timeWindowMillis) {
        this.timeWindowMillis = timeWindowMillis;
    }

    // 限制每台船每秒只能发送一条数据；做一下限流没必要传那么快,没啥用
    public boolean shipPushDataLimit(String clientId, String function) {
        String compositeKey = clientId + ":" + function;
        long currentTime = System.currentTimeMillis();

        CounterInfo counterInfo = counters.computeIfAbsent(compositeKey, k -> new CounterInfo());

        // 检查计数器是否过期
        if (currentTime - counterInfo.getLastUpdatedTime() > timeWindowMillis) {
            //重置计数器
            counterInfo.reset();
        }
        // 增加计数器
        int currentCount = counterInfo.incrementAndGet();

        // 检查是否超过阈值
        if (currentCount > 1) {
            // 如果超过阈值，减少计数器并返回 false
            counterInfo.decrementAndGet();
            return false;
        }

        return true;
    }
}