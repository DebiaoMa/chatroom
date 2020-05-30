package com.client.tools;

import com.client.view.Broadcast;
import com.client.view.Chat;
import com.client.view.GroupChat;
import org.omg.CORBA.WStringSeqHelper;

import java.util.HashMap;

/**
 * 管理用户聊天的类
 */
public class ManageChat {

    private static HashMap hashMap = new HashMap<String, Chat>();
    private static HashMap hashMapGroupChat = new HashMap<String, GroupChat>();

    //登陆的用户id
    public static void addChat(String logIdAndFriendId, Chat chat) {

        hashMap.put(logIdAndFriendId, chat);
    }

    public static void addGroupChat(String srcId, GroupChat groupChat) {
        hashMapGroupChat.put(srcId, groupChat);
    }


    public static Chat getChat(String logIdAndFriendId ) {

        return (Chat)hashMap.get(logIdAndFriendId);
    }

    public static GroupChat getGroupChat(String srcId) {

        return (GroupChat) hashMapGroupChat.get(srcId);
    }

}
