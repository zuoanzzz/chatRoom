package com.learn.client.service;

import com.learn.qqcommon.Message;

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
        while(true){
            try {
                System.out.println("客户端等待服务器的发送的消息。。。");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
