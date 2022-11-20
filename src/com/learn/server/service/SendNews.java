package com.learn.server.service;

import com.learn.common.Message;
import com.learn.common.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author zhoulei
 * @date 2022/11/20
 */
public class SendNews implements Runnable{

    @Override
    public void run() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入想推送的新闻（exit退出）:");
            String s = scanner.next();
            if (s.equals("exit"))
                break;
            Message message = new Message();
            message.setSender("Server");
            message.setMesType(MessageType.MESSAGE_SERVER_TO_ALL);
            message.setContent(s);

            HashMap<String, ServerThread> hm = ManageServerThreads.getHm();

            for (String s1 : hm.keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(s1).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
