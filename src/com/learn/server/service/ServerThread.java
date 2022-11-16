package com.learn.server.service;

import com.learn.qqcommon.Message;
import com.learn.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ServerThread extends Thread {
    private String uid;
    private Socket socket;

    public ServerThread(String uid, Socket socket) {
        this.socket = socket;
        this.uid = uid;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            System.out.println("服务端和客户端" + uid + "保持通信，读取数据。。。");
            try {
                Message message = (Message) ois.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    Message ret_userList = new Message();
                    ret_userList.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    ret_userList.setContent(ManageServerThreads.getOnlineUser());
                    ret_userList.setGetter(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(ret_userList);
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    ManageServerThreads.removeServerThread(uid);
                    System.out.println("服务端和客户端" + uid + "断开连接");
                    socket.close();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
