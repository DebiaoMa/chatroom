package com.client.tools;

import com.client.model.ClientConServerThread;

import java.util.HashMap;

/**
 * 管理客户端线程的类
 *
 */
public class ManageClientThread {

    private static HashMap hashMap = new HashMap<String, ClientConServerThread>();

    //把client端线程加入到hashmap中
    public static void addManageClientThread (String userId, ClientConServerThread clientConServerThread) {

        hashMap.put(userId, clientConServerThread);
    }

    public static ClientConServerThread getManageClientThread(String userId) {

        return (ClientConServerThread)hashMap.get(userId);
    }

    //删除client线程
    public static void deleteManageClientThread () {

        hashMap.clear();
    }
}
