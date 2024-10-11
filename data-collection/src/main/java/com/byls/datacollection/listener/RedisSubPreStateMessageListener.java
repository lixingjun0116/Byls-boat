package com.byls.datacollection.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * 文件名: RedisSubPreStateMessageListener
 * 创建时间:2019/9/29 10:32
 * 描述: pre链路状态消息订阅通道  链路掉线报警
 */
@Slf4j
@Component("preStateListener")
public class RedisSubPreStateMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] bytes) {

    }

    //    @Autowired
//    private AlarmSendUtil alarmService;
//    @Autowired
//    @Qualifier("slaveRedisTemplate")
//    private RedisTemplate<String, String> redisTemplate;
//    private RedisSerializer<?> serializer = new StringRedisSerializer();
//
//    public static volatile List<Integer> screenA = new ArrayList<Integer>() {{
//        for (int i = 0; i < 10; i++)
//            this.add(1);
//    }};
//    public static volatile List<Integer> screenB = new ArrayList<Integer>() {{
//        for (int i = 0; i < 10; i++)
//            this.add(1);
//    }};
//
//    @Override
//    public void onMessage(Message message, byte[] bytes) {
//        Object body = serializer.deserialize(message.getBody());
//        String msg = body.toString();
//        log.info("收到pre传来的链路信息--->{}", body.toString());
//        try {
//            List<JsonPreStateClass> receiveDatas = null;
//            if (msg.startsWith("[")) {
//                receiveDatas = JacksonUtil.json2list(msg, JsonPreStateClass.class);
//            } else if (msg.startsWith("{")) {
//                receiveDatas = new ArrayList<>();
//                receiveDatas.add(JacksonUtil.json2pojo(msg, JsonPreStateClass.class));
//            } else {
//                log.error("接收到的消息不符合规范");
//                throw new RuntimeException("接收到的消息不符合规范");
//            }
//            for (JsonPreStateClass receiveData : receiveDatas) {
//                int stateId = receiveData.getStateId();
//                int dacuId = receiveData.getDacuId();
//                int modelId = receiveData.getModelId();
//                String errMsg = receiveData.getCnMsg();
//                switch (stateId) {
//                    case 1: //连接成功
//                        handleOk(stateId, dacuId);
//                        break;
//                    case -1: // 连接失败
//                        handleInitFail(stateId, dacuId);
//                        break;
//                    case 4:
//                        log.error("点写入失败 {}", modelId);
//                        break;
//                    case 0:
//                        log.error("pre内核错误信息:{}", errMsg);
//                        handleDiaoxian(stateId, dacuId);
//                        break;
//                }
//            }
//            log.info("接收到redis消息{},json转换为{}", msg, receiveDatas);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("解析pre链路信息错误{}", e.getMessage());
//            log.error(DetailTrace.getTrace(e));
//        }
//    }
//
//
//    private String getVolumeStateKey(int dacuId) {
//        int idx = dacuId - SystemConstants.dacuId;
//        if (idx > 0 && idx < VirtualPointConfig.volumeStateKeys.size()) {
//            return VirtualPointConfig.volumeStateKeys.get(idx);
//        }
//        return null;
//    }
//
//    //设置体积仪链路状态
//    private void setVolumeDetectorLinkerState(int dacuId, boolean isConnected) {
//        String volumeId = getVolumeStateKey(dacuId);
//        if (!StringUtils.isEmpty(volumeId)) {
//            redisTemplate.opsForValue().set(volumeId, isConnected ? "1" : "0");
//        }
//    }
//
//    private void handleInitFail(int stateId, int dacuId) throws JsonProcessingException {
//        log.error("主链路连接失败，dacuId=" + dacuId);
//        if (dacuId >= 70 && dacuId < 81) {//体积仪
//            setVolumeDetectorLinkerState(dacuId, false);
//            return;
//        }
//        switch (dacuId) {
//            case 3:
//                sendAlarm(AlarmPointConfig.ALARM_CREVIESE_SERVER_LOST);
//                log.error("Crevis模块初始化连接失败");
//                break;
//            case 8:
//                sendAlarm(AlarmPointConfig.ALARM_THICKRAD_LOST);
//                log.error("西克厚度雷达控制单元连接失败");
//                break;
//            case 11:
//                screenError("A1", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                break;
//            case 12:
//                screenError("A2", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                break;
//            case 13:
//                screenError("A3", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                break;
//            case 14:
//                screenError("A4", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                break;
//            case 15:
//                screenError("A5", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                break;
//            case 16:
//                screenError("A6", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                break;
//            case 17:
//                screenError("A7", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                break;
//            case 18:
//                screenError("A8", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                break;
//            case 19:
//                screenError("A9", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                break;
//            case 20:
//                screenError("A10", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                break;
//            case 31:
//                screenError("B1", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                break;
//            case 32:
//                screenError("B2", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                break;
//            case 33:
//                screenError("B3", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                break;
//            case 34:
//                screenError("B4", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                break;
//            case 35:
//                screenError("B5", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                break;
//            case 36:
//                screenError("B6", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                break;
//            case 37:
//                screenError("B7", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                break;
//            case 38:
//                screenError("B8", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                break;
//            case 39:
//                screenError("B9", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                break;
//            case 40:
//                screenError("B10", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                break;
//            case 21:
//                sendAlarm(AlarmPointConfig.ALARM_PLC2_LOST);
//                log.error("现场ABPLC连接失败");
//                break;
//        }
//    }
//
//    private void handleOk(int stateId, int dacuId) throws JsonProcessingException {
//        log.error("主链路连接正常，dacuId=" + dacuId);
//        if (dacuId >= 70 && dacuId < 81) {//体积仪
//            setVolumeDetectorLinkerState(dacuId, true);
//        }
//        switch (dacuId) {
//            case 3:
//                log.info("Crevis模块连接");
//                removeAlarm(AlarmPointConfig.ALARM_CREVIESE_SERVER_LOST);
//                break;
//            case 8:
//                log.info("西克厚度雷达控制单元连接");
//                removeAlarm(AlarmPointConfig.ALARM_THICKRAD_LOST);
//                break;
//            case 11:
//                screenOk("A1", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                break;
//            case 12:
//                screenOk("A2", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                break;
//            case 13:
//                screenOk("A3", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                break;
//            case 14:
//                screenOk("A4", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                break;
//            case 15:
//                screenOk("A5", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                break;
//            case 16:
//                screenOk("A6", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                break;
//            case 17:
//                screenOk("A7", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                break;
//            case 18:
//                screenOk("A8", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                break;
//            case 19:
//                screenOk("A9", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                break;
//            case 20:
//                screenOk("A10", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                break;
//            case 31:
//                screenOk("B1", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                break;
//            case 32:
//                screenOk("B2", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                break;
//            case 33:
//                screenOk("B3", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                break;
//            case 34:
//                screenOk("B4", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                break;
//            case 35:
//                screenOk("B5", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                break;
//            case 36:
//                screenOk("B6", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                break;
//            case 37:
//                screenOk("B7", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                break;
//            case 38:
//                screenOk("B8", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                break;
//            case 39:
//                screenOk("B9", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                break;
//            case 40:
//                screenOk("B10", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                break;
//            case 21:
//                log.info("现场ABPLC连接");
//                removeAlarm(AlarmPointConfig.ALARM_PLC2_LOST);
//                break;
//        }
//        try {
//            Thread.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleDiaoxian(int stateId, int dacuId) throws JsonProcessingException {
//        log.error("主链路掉线，dacuId=" + dacuId);
//        if (dacuId >= 70 && dacuId < 81) {//体积仪
//            setVolumeDetectorLinkerState(dacuId, false);
//        }
//        if ("hdg_old".equals(redisTemplate.opsForValue().get("alarm"))) {
//            switch (dacuId) {
//                case 3:
//                    sendAlarm(AlarmPointConfig.ALARM_CREVIESE_SERVER_LOST);
//                    log.error("Crevis模块掉线");
//                    break;
//                case 8:
//                    sendAlarm(AlarmPointConfig.ALARM_THICKRAD_LOST);
//                    log.error("西克厚度雷达控制单元掉线");
//                    break;
//                case 21:
//                    screenError("A1", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                    break;
//                case 22:
//                    screenError("A2", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                    break;
//                case 23:
//                    screenError("A3", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                    break;
//                case 24:
//                    screenError("A4", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                    break;
//                case 25:
//                    screenError("A5", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                    break;
//                case 26:
//                    screenError("A6", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                    break;
//                case 27:
//                    screenError("A7", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                    break;
//                case 28:
//                    screenError("A8", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                    break;
//                case 29:
//                    screenError("A9", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                    break;
//                case 30:
//                    screenError("A10", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                    break;
//                case 31:
//                    screenError("B1", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                    break;
//                case 32:
//                    screenError("B2", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                    break;
//                case 33:
//                    screenError("B3", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                    break;
//                case 34:
//                    screenError("B4", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                    break;
//                case 35:
//                    screenError("B5", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                    break;
//                case 36:
//                    screenError("B6", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                    break;
//                case 37:
//                    screenError("B7", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                    break;
//                case 38:
//                    screenError("B8", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                    break;
//                case 39:
//                    screenError("B9", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                    break;
//                case 40:
//                    screenError("B10", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                    break;
//                case 11:
//                    sendAlarm(AlarmPointConfig.ALARM_PLC2_LOST);
//                    log.error("现场ABPLC掉线");
//                    break;
//            }
//        } else {
//            switch (dacuId) {
//                case 3:
//                    sendAlarm(AlarmPointConfig.ALARM_CREVIESE_SERVER_LOST);
//                    log.error("Crevis模块掉线");
//                    break;
//                case 8:
//                    sendAlarm(AlarmPointConfig.ALARM_THICKRAD_LOST);
//                    log.error("西克厚度雷达控制单元掉线");
//                    break;
//                case 11:
//                    screenError("A1", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                    break;
//                case 12:
//                    screenError("A2", VirtualPointConfig.ALARM_SERIAL_SERVERA1_LOST);
//                    break;
//                case 13:
//                    screenError("A3", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                    break;
//                case 14:
//                    screenError("A4", VirtualPointConfig.ALARM_SERIAL_SERVERA2_LOST);
//                    break;
//                case 15:
//                    screenError("A5", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                    break;
//                case 16:
//                    screenError("A6", VirtualPointConfig.ALARM_SERIAL_SERVERA3_LOST);
//                    break;
//                case 17:
//                    screenError("A7", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                    break;
//                case 18:
//                    screenError("A8", VirtualPointConfig.ALARM_SERIAL_SERVERA4_LOST);
//                    break;
//                case 19:
//                    screenError("A9", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                    break;
//                case 20:
//                    screenError("A10", VirtualPointConfig.ALARM_SERIAL_SERVERA5_LOST);
//                    break;
//                case 31:
//                    screenError("B1", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                    break;
//                case 32:
//                    screenError("B2", VirtualPointConfig.ALARM_SERIAL_SERVERB1_LOST);
//                    break;
//                case 33:
//                    screenError("B3", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                    break;
//                case 34:
//                    screenError("B4", VirtualPointConfig.ALARM_SERIAL_SERVERB2_LOST);
//                    break;
//                case 35:
//                    screenError("B5", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                    break;
//                case 36:
//                    screenError("B6", VirtualPointConfig.ALARM_SERIAL_SERVERB3_LOST);
//                    break;
//                case 37:
//                    screenError("B7", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                    break;
//                case 38:
//                    screenError("B8", VirtualPointConfig.ALARM_SERIAL_SERVERB4_LOST);
//                    break;
//                case 39:
//                    screenError("B9", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                    break;
//                case 40:
//                    screenError("B10", VirtualPointConfig.ALARM_SERIAL_SERVERB5_LOST);
//                    break;
//                case 21:
//                    sendAlarm(AlarmPointConfig.ALARM_PLC2_LOST);
//                    log.error("现场ABPLC掉线");
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 光幕报错
//     *
//     * @param key
//     * @param screenSerialId 光幕标识
//     * @throws JsonProcessingException
//     */
//    private void screenError(String key, String screenSerialId){
//        log.error("串口服务器断开连接:{}", key);
//        sendAlarm(screenSerialId);
//        redisTemplate.opsForValue().set(screenSerialId, 1 + "");
//        if(key.startsWith("A")){
//            redisTemplate.opsForValue().set(VirtualPointConfig.SCREEN_A_ERROR,1+"");
//        }else {
//            redisTemplate.opsForValue().set(VirtualPointConfig.SCREEN_B_ERROR,1+"");
//        }
//    }
//
//    private void screenOk(String key, String screenSerialId) {
//        log.info("串口服务器连接成功:{}", key);
//        // 消除告警
//        removeAlarm(screenSerialId);
//        redisTemplate.opsForValue().set(screenSerialId, 0 + "");
//    }
//
//    /**
//     * 发送报警
//     *
//     * @param alarmId
//     */
//    private void sendAlarm(String alarmId) {
//        try {
//            alarmService.sendAlarm(alarmId);
//        } catch (Exception e) {
//            log.error("报警模块错误{}", e.getMessage());
//        }
//    }
//
//    /**
//     * 消除报警
//     */
//    private void removeAlarm(String alarmId) {
//        try {
//            alarmService.removeAlarm(alarmId);
//        } catch (Exception e) {
//            log.error("报警模块错误{}", e.getMessage());
//        }
//    }

}
