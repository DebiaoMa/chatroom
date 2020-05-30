package com.client.tools;

import com.client.view.Logged;

import java.util.HashMap;

/**
 * 管理好友列表
 */
public class ManageFriendList {


    private static HashMap hashMap = new HashMap<String, Logged>();

    public static void addFriendList(String srcId, Logged logged) {
        hashMap.put(srcId, logged);
    }

    public static Logged getLogged(String srcId) {
        return (Logged) hashMap.get(srcId);
    }

    public static HashMap getHashMap() {
        return hashMap;
    }
}
