package com.learn.client.service;

import java.util.HashMap;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ManageClientThreads {
    private static HashMap<String,ClientThread> hm = new HashMap<>();

    public static void addClientThread(String uid,ClientThread clientThread){
        hm.put(uid,clientThread);
    }

    public static ClientThread getClientThread(String uid){
        return hm.get(uid);
    }
}
