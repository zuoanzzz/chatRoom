package com.learn.client.service;

import com.learn.common.Message;
import com.learn.common.MessageType;
import com.learn.common.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

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
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);

            //object IO流会自带结束标志

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

    public void onlineFriendList() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUID());
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getClientThread(u.getUID())
                    .getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUID());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
            System.exit(0);     //结束进程
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToOne(String content, String senderId, String getterId) {
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setContent(content);
        message.setGetter(getterId);
        message.setSender(senderId);
        message.setSendTime(new Date().toString());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToAll(String content,String senderId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
        message.setContent(content);
        message.setSender(senderId);
        message.setSendTime(new Date().toString());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFileToOne(String src,String dest,String senderId,String getterId){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //读取文件
        byte[] bytes = new byte[(int) new File(src).length()];      //通过File类的方法获取文件的长度,一次读取完

        try {
            FileInputStream fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);
            message.setFileBytes(bytes);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
