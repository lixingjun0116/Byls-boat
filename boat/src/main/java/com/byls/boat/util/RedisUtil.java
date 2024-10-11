package com.byls.boat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 设置键值对，没有过期时间
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 设置键值对，有过期时间
    public boolean set(String key, String value, long expire, TimeUnit unit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, expire, unit);
    }

    // 获取键对应的值
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    // 删除键
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    // 判断键是否存在
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    // 设置过期时间
    public boolean expire(String key, long expire, TimeUnit unit) {
        return stringRedisTemplate.expire(key, expire, unit);
    }

    // 获取过期时间
    public Long getExpire(String key, TimeUnit unit) {
        return stringRedisTemplate.getExpire(key, unit);
    }
}
