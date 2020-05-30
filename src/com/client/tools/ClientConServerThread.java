package com.client.tools;

import com.client.view.Chat;
import com.client.view.Logged;
import com.common.Message;
import com.common.MessageType;

import java.io.ObjectInputStream;
import java.net.Socket;


/**
 * 客户端和服务器通信的线程
 */
public class ClientConServerThread extends Thread{

    private Socket socket;

    public ClientConServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {

            try {
                //处理所有收到的消息
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
                Message msg = (Message) ois.readObject();

                if (msg.getMesType().equals(MessageType.messageCommMsg)) {

                    dealCommMsg(msg);
                } else if (msg.getMesType().equals(MessageType.messageOnlineFriend)) {

                    dealOnlineFriendMsg(msg);
                } else if (msg.getMesType().equals(MessageType.messageBroadcast)) {

                    dealBroadcastMsg(msg);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void dealCommMsg(Message msg) {

        System.out.println("come from " + msg.getSender() +":" + msg.getCon());
        System.out.println("comm:" + msg.getGetter() + msg.getSender());
        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());  //myId + senderId 来唯一确定聊天窗口
        chat.showMessage(msg);
    }

    public void dealOnlineFriendMsg(Message msg) {

        System.out.println(msg.toString());
        System.out.println(ManageFriendList.getHashMap());
        Logged logged = ManageFriendList.getLogged(msg.getGetter());
        logged.updateFriend(msg);
    }

    public void dealBroadcastMsg(Message msg) {

        System.out.println("come from " + msg.getSender() +"的广播:" + msg.getCon());
        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());  //myId + senderId 来唯一确定聊天窗口
        chat.showMessage(msg);
    }

}
