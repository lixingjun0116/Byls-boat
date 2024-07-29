package com.byls.datacollection.config;

import com.byls.util.constant.RedisChannelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;

/**
 * RedisListenerConfig
 */
@Configuration
@DependsOn()
public class RedisListenerConfig {

    /**
     * pre 实时数据更改的监听
     */
    @Resource(name = "normalListener")
    private MessageListener normalListener;

//    /**
//     * pre报警通道的监听
//     */
//    @Resource(name = "alarmListener")
//    private MessageListener alarmListener;

    @Resource(name = "preStateListener")
    private MessageListener preStateListener;

//    @Resource(name = "initDeviceListener")
//    private MessageListener initDeviceListener;

    /**
     * 配置redis订阅  与pre交互
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    RedisMessageListenerContainer containerPre(@Autowired @Qualifier("slaveRedisTemplate") RedisTemplate redisTemplate) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        container.addMessageListener(normalListener, new ChannelTopic(RedisChannelConstants.PRE_POINT_MSG_CHANNEL));
        container.addMessageListener(preStateListener, new ChannelTopic(RedisChannelConstants.PRE_STATE_CHANNEL));
//        container.addMessageListener(initDeviceListener, new ChannelTopic(RedisChannelConstants.INIT_DEVICE_CALLBACK_CHANNEL));
//        container.addMessageListener(alarmListener, new ChannelTopic(RedisChannelConstants.PRE_ALARM_CHANNEL));
        return container;
    }

}
