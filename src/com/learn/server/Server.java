package com.learn.server;

import com.learn.common.Message;
import com.learn.common.MessageType;
import com.learn.common.User;
import com.learn.server.service.ManageServerThreads;
import com.learn.server.service.SendNews;
import com.learn.server.service.ServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class Server {
    private ServerSocket serverSocket;
    //hashmap线程不安全（当多个线程有修改用户集的需求时）
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();   //合法用户集
    private static ConcurrentHashMap<String, ArrayList<Message>> offLineMessages = new ConcurrentHashMap<>();

    static {     //在静态代码块，初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));

        offLineMessages.put("100", new ArrayList<>());
        offLineMessages.put("200", new ArrayList<>());
        offLineMessages.put("300", new ArrayList<>());
        offLineMessages.put("至尊宝", new ArrayList<>());
        offLineMessages.put("紫霞仙子", new ArrayList<>());
        offLineMessages.put("菩提老祖", new ArrayList<>());
    }

    public static ConcurrentHashMap<String, ArrayList<Message>> getOffLineMessages() {
        return offLineMessages;
    }

    public static void main(String[] args) {
        new Thread(new SendNews()).start();
        new Server();
    }

    public boolean checkUser(String uid, String pwd) {
        User user = validUsers.get(uid);
        if (user == null) {
            return false;
        } else if (!user.getPwd().equals(pwd)) {
            return false;
        }
        return true;
    }

    public Server() {
        System.out.println("服务器端在9999端口监听");
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();
                Message message = new Message();
                if (checkUser(u.getUID(), u.getPwd())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    //创建线程，和客户端保持通信
                    if (!offLineMessages.get(u.getUID()).isEmpty()) {
                        ArrayList<Message> messages = offLineMessages.get(u.getUID());
                        for (Message message1 : messages) {
                            message1.setMesType(MessageType.MESSAGE_OFFLINE_MES);
                            //一个对象输出流一次发送多个对象时，只有第一个会有header，因此不能正常读取，需要每次使用不同的对象输出流
                            new ObjectOutputStream(socket.getOutputStream()).writeObject(message1);
                        }
                    }
                    offLineMessages.remove(u.getUID());
                    ServerThread serverThread = new ServerThread(u.getUID(), socket);
                    serverThread.start();
                    ManageServerThreads.addServerThread(u.getUID(), serverThread);
                } else {
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAILED);
                    oos.writeObject(message);
                    socket.close();     //一定是在socket关闭之前传输完所有想传输的信息！！
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}