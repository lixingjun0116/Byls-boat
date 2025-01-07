//package com.byls.boat.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//import org.java_websocket.enums.ReadyState;
//
//import java.net.URI;
//
//@Slf4j
//public class PersistentHardwareWebSocketClient extends WebSocketClient {
//
//    private static final int MAX_RETRIES = 5;
//    private static final long RETRY_DELAY = 5000; // 毫秒
//    private URI serverUri;
//
//    private PersistentHardwareWebSocketClient(URI serverUri) {
//        super(serverUri);
//        this.serverUri = serverUri;
//    }
//
//    public static PersistentHardwareWebSocketClient getInstance(URI serverUri) {
//        return new PersistentHardwareWebSocketClient(serverUri);
//    }
//
//    public void connectAndReconnect() {
//        this.connect();
//    }
//
//    public void reconnect() {
//        int retries = 0;
//        while (retries < MAX_RETRIES) {
//            try {
//                Thread.sleep(RETRY_DELAY);
//                // 检查当前客户端的状态
//                if (!this.getReadyState().equals(ReadyState.OPEN)) {
//                    // 创建一个新的 WebSocket 客户端实例
//                    PersistentHardwareWebSocketClient newClient = PersistentHardwareWebSocketClient.getInstance(serverUri);
//                    // 使用新的客户端实例尝试连接
//                    newClient.connect();
//                    return;
//                } else {
//                    return;
//                }
//            } catch (InterruptedException e) {
//                retries++;
//                log.info("尝试重新连接 {} 失败重试…", retries);
//            } catch (IllegalStateException ise) {
//                log.error("WebSocket 客户端不可重用: {}", ise.getMessage());
//                retries++;
//            } catch (Exception e) {
//                log.error("发生错误: {}", e.getMessage());
//                retries++;
//            }
//        }
//        log.error("已达到的最大重试次数。无法重新建立连接。");
//    }
//
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        log.info("已连接硬件服务器");
//    }
//
//    @Override
//    public void onMessage(String message) {
//        log.info("收到硬件传来的消息: " + message);
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        log.info("连接已关闭 " + (remote ? "远程对等" : "us") + ".");
//        // 自动重连逻辑
//        reconnect();
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        log.error("websocket发生异常: {}", ex.getMessage());
//    }
//
//    public ReadyState getReadyState() {
//        return super.getReadyState();
//    }
//}
