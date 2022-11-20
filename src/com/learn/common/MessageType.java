package com.learn.common;

import com.learn.server.Server;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1";
    String MESSAGE_LOGIN_FAILED = "2";
    String MESSAGE_COMM_MES = "3";  //普通信息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //获取在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5"; //返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6";   //客户端请求退出
    String MESSAGE_TO_ALL_MES = "7";
    String MESSAGE_FILE_MES = "8";
    String MESSAGE_SERVER_TO_ALL = "9";
    String MESSAGE_OFFLINE_MES = "10";
}
