package com.byls.util.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lixu
 * @version 1.0
 * @date 2019/12/12 18:07
 */
public class PreRedisUtil {

    private static RedisTemplate<String, String> redisTemplate;

    static {
        try {
            redisTemplate = SpringUtils.getBean("slaveRedisTemplate", StringRedisTemplate.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static String writeValue(String key, String value) {
        return redisTemplate.opsForValue().getAndSet(key, value + "");
    }

    public static String checkAndSet(String key, String defaultValue) {
        try {
            if (!redisTemplate.hasKey(key)) {
                redisTemplate.opsForValue().set(key, defaultValue);
                return defaultValue;
            } else {
                return writeValue(key, defaultValue);
            }
        }catch (Exception ex){

        }
        return writeValue(key, defaultValue);
    }

    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public static void checkAndSetIndex(String key, String defaultValue) {
        if (!redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, defaultValue);
        }
        if (redisTemplate.opsForValue().get(key) == null) {
            redisTemplate.opsForValue().getAndSet(key, defaultValue);
        }
    }

    public static void checkAndSetDefaultValue(String key, String defaultValue) {
        try {
            if (!redisTemplate.hasKey(key)) {
                redisTemplate.opsForValue().set(key, defaultValue);
            }
        }catch (Exception ex){

        }
    }

    public static void writeValues(Map<String, String> slaveMap) {
        redisTemplate.opsForValue().multiSet(slaveMap);
    }

    public static void sendMessage(String sysAlarmChannel, String obj2json) {
        redisTemplate.convertAndSend(sysAlarmChannel, obj2json);
    }

    public static void increment(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }

    public static void writeValue(String key, String value,long time,TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    public static boolean deleteKey(String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
