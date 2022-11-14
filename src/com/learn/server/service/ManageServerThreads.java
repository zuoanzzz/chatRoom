package com.learn.server.service;

import java.util.HashMap;

/**
 * @author zhoulei
 * @date 2022/11/14
 */
public class ManageServerThreads {
    private static HashMap<String, ServerThread> hm = new HashMap<>();

    public static void addServerThread(String uid, ServerThread serverThread) {
        hm.put(uid, serverThread);
    }

    public static ServerThread getServerThread(String uid) {
        return hm.get(uid);
    }
}
