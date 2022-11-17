package com.learn.client.service;

import com.learn.common.Message;
import com.learn.common.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ClientThread extends Thread{
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
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(true){
            try {
                System.out.println("客户端等待服务器发送的消息。。。");
                Message message = (Message) ois.readObject();
                if(message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    String[] onlineUsers = message.getContent().split(" ");
                    System.out.println("==========在线用户列表==========");
                    for (String onlineUser : onlineUsers) {
                        System.out.println(onlineUser);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
