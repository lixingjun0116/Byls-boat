package com.byls.datacollection.service;

import com.alibaba.fastjson.JSONObject;
import com.byls.datacollection.pojo.bo.SignalMessage;
import com.byls.util.util.MasterPreRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 项目名: TLS
 * 文件名: WebSocketServer
 * 创建者: Administrator
 * 创建时间: 2020/8/4 11:11
 * 描述: websocket
 **/
@Slf4j
@ServerEndpoint(value = "/boat/signal")
@Component
public class WebSocketServer {

////    @Autowired
//    private static AppReadService appReadService;
//
//    @Autowired
//    public void setChatService(AppReadService appReadService) {
//        WebSocketServer.appReadService = appReadService;
//    }

    @PostConstruct
    public void init() {
        log.info("websocket 加载");
    }
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
        SendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

//        TrainMessageInfoVO messageInfo = new TrainMessageInfoVO();
//        ResultInfo result = new ResultInfo();
        log.info("来自客户端的消息：{}",message);
        // 处理message
        handleMessage(session,message);
//        try {
//            try {
//                messageInfo = appReadService.getTrainMessageInfo();
//                result.setCode(0);
//                result.setErrorMsg("获取成功");
//                result.setMessageInfo(messageInfo);
//                SendMessage(session, JacksonUtil.obj2json(result));
//            } catch (MtException e) {
//                result.setCode(-1);
//                result.setErrorMsg(e.getMessage());
//                result.setMessageInfo(null);
//                SendMessage(session, JacksonUtil.obj2json(result));
//            }
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleMessage(Session session, String message) {
        // 数据转换成Json结构体
        SignalMessage signalMessage = JSONObject.parseObject(message, SignalMessage.class);
        // 把数据写入到redis
        MasterPreRedisUtil.writeValue("x",signalMessage.getX().toString());
        MasterPreRedisUtil.writeValue("y",signalMessage.getY().toString());

//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            log.error("发送消息出错：{}", e.getMessage());
//            e.printStackTrace();
        }

}
