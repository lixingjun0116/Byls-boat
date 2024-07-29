package com.byls.datacollection.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 *
 * 描述: pre数据通道订阅
 */
@Component("normalListener")
@Slf4j
public class RedisSubPreNormalMessageListener implements MessageListener {

//    @Autowired
//    private SystemCacheService systemCacheService;
//    @Autowired
//    private DataHandlingService dataHandlingService;
//    @Autowired
//    private PREValueWriteRedisService preValueWriteRedisService;

    private RedisSerializer<?> serializer = new StringRedisSerializer();

    @Override
    public void onMessage(Message message, byte[] bytes) {
        Object body = serializer.deserialize(message.getBody());
        try {
            String msg = body.toString();
//            Map<Integer, Object> dataMap = super.getIntegerNumberMap(msg);
//            Map<String, Object> rawDataMap = super.getStringNumberMap(msg);
////            doFilter(dataMap);
//            // 进行数据分发
////            dataHandlingService.handlingMachineData(dataMap);
//            // 对systemCache 中的实时数据进行更新
//            systemCacheService.updateActualData(dataMap);
////            将实时数据更新到redis中
////            log.debug("监听到pre中的数据是："+ JacksonUtil.obj2json(dataMap));
//            preValueWriteRedisService.writeRedis(dataMap);
//            preValueWriteRedisService.writeStrRedis(rawDataMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收到redis消息{},json转换出现error", body.toString());
        }
    }

}
