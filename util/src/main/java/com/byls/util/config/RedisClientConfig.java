package com.byls.util.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 *项目名: tls
 *文件名: RedisTemplateConfig
 *创建者: mt-liuy liuyang@tjmeiteng.com
 *创建时间:2019/4/19 13:31
 *描述: redis客户端的配置
 * 定义了
 * 1.与pre交互的redis客户端
 * 2.系统缓存中的客户端
 */
@Configuration
public class RedisClientConfig {

    private final String factorySlave = "factorySlave";

    /**
     * 用来监听pre实时数据的客户端
     * @param factoryA
     * @return
     */
    @Bean(name = "masterRedisTemplate")
    public StringRedisTemplate rtdRedisClientTemplate(@Autowired@Qualifier("factoryMaster")LettuceConnectionFactory factoryA){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryA);
        return template;
    }

    /**
     * 用来监听另一个站实时数据的客户端
     * @param factoryE
     * @return
     */
    @Bean(name = "masterAnotherRedisTemplate")
    public StringRedisTemplate rtdAnotherRedisClientTemplate(@Autowired@Qualifier("factoryMasterAnother")LettuceConnectionFactory factoryE){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryE);
        return template;
    }


    /**
     * 用于系统实时消息的客户端
     * @param factoryB
     * @return
     */
    @Bean(name="slaveRedisTemplate")
    public StringRedisTemplate systemClientRedis(@Autowired@Qualifier("factorySlave") LettuceConnectionFactory factoryB){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryB);
        return template;
    }

    /**
     * 用于系统实时消息的客户端
     * @param factoryC
     * @return
     */
    @Bean(name="positionRedisTemplate")
    public StringRedisTemplate positionClientRedis(@Autowired@Qualifier("factoryPosition") LettuceConnectionFactory factoryC){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryC);
        return template;
    }

    /**
     * 用来监听云商接口的客户端
     * @param factoryD
     * @return
     */
//    @Conditional(XbdEnvCondition.class)
    @Bean(name = "apiRedisTemplate")
    public StringRedisTemplate apiRedisClientTemplate(@Autowired @Qualifier("factoryApi")LettuceConnectionFactory factoryD){
        StringRedisTemplate template = getRedisTemplate();
        template.setConnectionFactory(factoryD);
        return template;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    @Scope(value = "prototype")
    public GenericObjectPoolConfig redisPool(){
        return new GenericObjectPoolConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis-master")
    public RedisStandaloneConfiguration redisConfigA(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis-slave")
    public RedisStandaloneConfiguration redisConfigB(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis-position")
    public RedisStandaloneConfiguration redisConfigC(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
//    @Conditional(XbdEnvCondition.class)
    @ConfigurationProperties(prefix = "spring.redis.redis-api")
    public RedisStandaloneConfiguration redisConfigD(){
        return new RedisStandaloneConfiguration();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.redis-master-another")
    public RedisStandaloneConfiguration redisConfigE(){
        return new RedisStandaloneConfiguration();
    }

    @Bean("factoryMaster")
    @Primary
    public LettuceConnectionFactory factoryA(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigA){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigA, clientConfiguration);
    }

    @Bean("factorySlave")
    public LettuceConnectionFactory factoryB(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigB){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigB, clientConfiguration);
    }

    @Bean("factoryPosition")
    public LettuceConnectionFactory factoryC(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigC){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigC, clientConfiguration);
    }

    @Bean("factoryApi")
//    @Conditional(XbdEnvCondition.class)
    public LettuceConnectionFactory factoryD(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigD){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigD, clientConfiguration);
    }

    @Bean("factoryMasterAnother")
    public LettuceConnectionFactory factoryE(GenericObjectPoolConfig config, RedisStandaloneConfiguration redisConfigE){
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
                .poolConfig(config).commandTimeout(Duration.ofMillis(config.getMaxWaitMillis())).build();
        return new LettuceConnectionFactory(redisConfigE, clientConfiguration);
    }

    private StringRedisTemplate getRedisTemplate(){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}