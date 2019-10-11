package com.chen.fy.wisdomscenicspot.consts;

/**
 * 常量类
 */
public class Consts {

    /**
     * 熊：服务器url
     */
    public static final String FEEDBACK_SERVER_URL = "http://47.102.153.115:8080/isa/feedback";
    /**
     * 熊：WebSocket的url
     */
    public static final String RATE_FLOW_URL = "ws://47.102.153.115:8080/isa/websocket";

    /**
     * 拍照识别服务器
     */
    public static final String SCENIC_IDENTIFY_SERVER_URL = "http://47.102.153.115:8989/infer";

    /**
     * 大数据服务器_重庆
     */
    public static final String BIG_DATA_SERVER_URL_CQ = "http://47.102.153.115:8877/pre";

    /**
     * 大数据服务器_上海
     */
    public static final String BIG_DATA_SERVER_URL_SH = "http://47.102.153.115:8877/spre";

    /**
     * 普通socket上传图片实现拍照识别
     */
    public static final String SOCKET_URL = "47.102.153.115";
    public static final int PORT = 8989;
}
