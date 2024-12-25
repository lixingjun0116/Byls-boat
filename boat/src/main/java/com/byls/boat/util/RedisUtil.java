// RedisUtil.java
package com.byls.boat.util;

import com.byls.boat.config.redis.RedisConfig;
import com.byls.boat.constant.BoatType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void setByType(BoatType boatType, String key, String value) {
        RedisTemplate<Object, Object> redisTemplate = redisConfig.getRedisTemplateByBoatType(boatType.getType());
        redisTemplate.opsForValue().set(key, value);
    }

    public String getByType(BoatType boatType, String key) {
        RedisTemplate<Object, Object> redisTemplate = redisConfig.getRedisTemplateByBoatType(boatType.getType());
        return (String) redisTemplate.opsForValue().get(key);
    }


    // 设置键值对，没有过期时间
    public void setValue(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // 设置键值对，有过期时间
    public boolean setValue(String key, String value, long expire, TimeUnit unit) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, value, expire, unit);
    }

    // 获取键对应的值
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    // 删除键
    public void deleteValue(String key) {
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

    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value != null) {
            try {
                return objectMapper.readValue(value, new TypeReference<List<T>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException("从redis获取list数据失败", e);
            }
        }
        return null;
    }

    public void setList(String key, List<?> list) {
        try {
            String json = objectMapper.writeValueAsString(list);
            stringRedisTemplate.opsForValue().set(key, json);
        } catch (IOException e) {
            throw new RuntimeException("存储list到redis失败", e);
        }
    }

    /**
     * 将 Map 集合缓存到 Redis 中
     *
     * @param key   缓存键
     * @param value 缓存值
     */

    public <K, V> void setHash(String key, Map<K, V> value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("键或值不能为空");
        }

        // 创建一个新的 Map 来存储转换后的键值对
        Map<String, String> stringMap = new HashMap<>();

        for (Map.Entry<K, V> entry : value.entrySet()) {
            K keyEntry = entry.getKey();
            V valueEntry = entry.getValue();

            if (keyEntry == null || valueEntry == null) {
                throw new IllegalArgumentException("映射中的键或值不能为空");
            }

            // 将键和值转换为 String 类型
            String keyStr = keyEntry.toString();
            String valueStr = valueEntry.toString();

            stringMap.put(keyStr, valueStr);
        }

        try {
            stringRedisTemplate.opsForHash().putAll(key, stringMap);
        } catch (Exception e) {
            throw new RuntimeException("存储hash到redis失败", e);
        }
    }



    /**
     * 从 Redis 中获取 Hash 结构的数据
     *
     * @param key 缓存键
     * @return 缓存值
     */
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getHash(String key) {
        return (Map<K, V>) stringRedisTemplate.opsForHash().entries(key);
    }

}
