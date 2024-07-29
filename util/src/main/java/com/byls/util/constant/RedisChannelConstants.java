package com.byls.util.constant;

/**
 *
 * 描述: 系统关注的通道的配置
 */
public class RedisChannelConstants {

    /**
     * pre数据通道
     */
    public static final String PRE_POINT_MSG_CHANNEL = "sendChannel";
    /**
     * 向pre发送命令的通道
     */
    public static final String PRE_COMMAND_CHANNEL = "receiveChannel";
    /**
     * 接收pre报警的通道
     */
    public static final String PRE_ALARM_CHANNEL = "sendAlarmChannel";
    /**
     * 接收pre的链路通道 sendPreStateChannel
     */
    public static final String PRE_STATE_CHANNEL = "sendPreStateChannel";
    /**
     * 设备的初始化状态
     */
    public static final String INIT_DEVICE_CHANNEL = "receiveFindChannel";
    /**
     * 设备的初始化状态返回值
     */
    public static final String INIT_DEVICE_CALLBACK_CHANNEL = "sendPreDacuMsgChannel";

    /**
     * 接收装车各个系统报警消息的通道
     */
    public static final String SYS_ALARM_CHANNEL = "TLSAlarmChannel";

    /**
     * 接收装车各个系统报警消息回调的通道
     */
    public static final String ALARM_CALL_BACK_CHANNEL = "TLSAlarmCallBackChannel";

    /**
     * 通知装车系统，系统参数变更的消息通道
     */
    public static final String SYS_PARAM_CHANNEL = "TLSParamChannel";
    /**
     * 通知装车系统，体积仪的消息通道
     */
    public static final String SYS_VOL_CHANNEL = "VolChannel";
    /**
     * 通知定位程序,车头已到位(单线倒车的装车站使用)
     */
    public static final String SYS_CONTROL_CHANNEL = "TLSControlChannel";
    /**
     * 通知PAD语音播报的消息通道
     */
    public static final String SYS_PAD_CHANNEL = "TLSPadChannel";

    /**
     * 给云商推送报警信息通道
     * 内环
     */
    public static final String XBD_IN_ALARM_CHANNEL = "sendXbdInAlarmChannel";

    /**
     * 给云商推送装车状态通道
     * 内环
     */
    public static final String XBD_IN_TASK_CHANNEL = "sendXbdInTaskChannel";

    /**
     * 给云商推送设备信息通道
     * 内环
     */
    public static final String XBD_IN_DEVICE_CHANNEL = "sendXbdInDeviceChannel";

    /**
     * 给云商推送识别的车厢信息
     * 内环
     */
    public static final String XBD_IN_CARRIAGE_CHANNEL = "sendXbdInCarriageChannel";

    /**
     * 给云商推送订单信息通道
     * 内环
     */
    public static final String XBD_IN_TASK_INFO_CHANNEL = "sendXbdInTaskInfoChannel";

    /**
     * 给云商推送报警信息通道
     * 外环
     */
    public static final String XBD_OUT_ALARM_CHANNEL = "sendXbdOutAlarmChannel";

    /**
     * 给云商推送装车状态通道
     * 外环
     */
    public static final String XBD_OUT_TASK_CHANNEL = "sendXbdOutTaskChannel";

    /**
     * 给云商推送设备信息通道
     * 外环
     */
    public static final String XBD_OUT_DEVICE_CHANNEL = "sendXbdOutDeviceChannel";

    /**
     * 给云商推送识别的车厢信息
     * 外环
     */
    public static final String XBD_OUT_CARRIAGE_CHANNEL = "sendXbdOutCarriageChannel";

    /**
     * 给云商推送订单信息通道
     * 内环
     */
    public static final String XBD_OUT_TASK_INFO_CHANNEL = "sendXbdOutTaskInfoChannel";
}
