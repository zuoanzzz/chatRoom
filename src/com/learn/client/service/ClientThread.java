package com.learn.client.service;

import com.learn.common.Message;
import com.learn.common.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ClientThread extends Thread {
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                System.out.println("客户端等待服务器发送的消息。。。");
                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)) {
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("==========在线用户列表==========");
                    for (String onlineUser : onlineUsers) {
                        System.out.println(onlineUser);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println(message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)) {
                    System.out.println("群发消息：" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    String dest = message.getDest();
                    FileOutputStream fileOutputStream = new FileOutputStream(dest);
                    fileOutputStream.write(message.getFileBytes());
                } else if (message.getMesType().equals(MessageType.MESSAGE_SERVER_TO_ALL)) {
                    System.out.println("服务器公告：" + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_OFFLINE_MES)) {
                    System.out.println("用户（" + message.getGetter() + "）发给你的离线消息：" + message.getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
