package com.learn.server.service;

import com.learn.qqcommon.Message;

import java.io.ObjectInputStream;
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
        while (true) {
            System.out.println("服务端和客户端保持通信，读取数据。。。");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
