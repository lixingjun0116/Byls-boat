package com.byls.datacollection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/*
 *
 *描述: redis客户端
 */
@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplate")
    @Primary
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        //键的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //值的序列化方式
        template.setValueSerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        /**必须执行这个函数,初始化RedisTemplate*/
        template.afterPropertiesSet();
        return template;
    }

}
