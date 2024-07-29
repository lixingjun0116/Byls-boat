package com.byls.util.util;

import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lixu
 * @version 1.0
 * @date 2019/12/12 18:07
 */
@Component
@DependsOn("SpringBeansUtils")
public class MasterPreRedisUtil {

    private static RedisTemplate<String,String> redisTemplate;

    static {
        try {
            redisTemplate = SpringUtils.getBean("masterRedisTemplate", StringRedisTemplate.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public static String writeValue(String key,String value){
        return redisTemplate.opsForValue().getAndSet(key,value+"");
    }

    public static List<String> getValues(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public static void writeValues(Map<String, String> dataMap) {
        redisTemplate.opsForValue().multiSet(dataMap);
    }

    public static boolean set(String key, String value) {
        if (!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        return false;
    }

    public static String checkAndSet(String key,String defaultValue){
        if(!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(key,defaultValue);
            return defaultValue;
        }else{
            return writeValue(key,defaultValue);
        }
    }

    public static void checkAndSetDefaultValue(String key,String defaultValue){
        if(!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(key,defaultValue);
        }
    }

    public static String getCheckValue(String key){
        if(!redisTemplate.hasKey(key)){
            redisTemplate.opsForValue().set(key,"144");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public static void expire(String key, String val) {
        redisTemplate.opsForValue().set(key,val,30, TimeUnit.SECONDS);
    }

    public static void expire(String key, String val,Integer time) {
        redisTemplate.opsForValue().set(key,val,time, TimeUnit.SECONDS);
    }

    public static Long getExpireTime(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
