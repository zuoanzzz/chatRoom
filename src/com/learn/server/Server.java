package com.learn.server;

import com.learn.qqcommon.Message;
import com.learn.qqcommon.MessageType;
import com.learn.qqcommon.User;
import com.learn.server.service.ManageServerThreads;
import com.learn.server.service.ServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class Server {
    private ServerSocket serverSocket;

    public Server() {
        System.out.println("服务器端在9999端口监听");
        try {
            serverSocket = new ServerSocket(9999);
            while (true){
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();
                Message message = new Message();
                if(u.getUID().equals("100")&&u.getPwd().equals("123456")){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //创建线程，和客户端保持通信
                    ServerThread serverThread = new ServerThread(u.getUID(), socket);
                    serverThread.start();
                    ManageServerThreads.addServerThread(u.getUID(), serverThread);
                }else {
                    socket.close();
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAILED);
                }
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}