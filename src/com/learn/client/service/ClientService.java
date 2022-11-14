package com.learn.client.service;

import com.learn.qqcommon.Message;
import com.learn.qqcommon.MessageType;
import com.learn.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ClientService {
    private Socket socket;
    private User u = new User();

    public boolean checkUser(String uid, String pwd) {
        u.setUID(uid);
        u.setPwd(pwd);
        Message message = null;
        try {
            socket = new Socket(InetAddress.getByName("10.3.127.75"), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            //需要结束标志吗？

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            message = (Message) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {
            //创建一个与服务器保持通信的线程
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();
            ManageClientThreads.addClientThread(uid, clientThread);
            return true;
        } else {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
