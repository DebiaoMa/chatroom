package com.client.model;

import com.client.tools.ManageChat;
import com.client.tools.ManageFriendList;
import com.client.tools.Reg;
import com.client.view.Chat;
import com.client.view.Logged;
import com.common.Message;
import com.common.MessageType;

import java.awt.*;
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
                } else if (msg.getMesType().equals(MessageType.messageReceiveFile)) {

                    dealReceiveFile(msg);
                } else if (msg.getMesType().equals(MessageType.messageIp)) {

                    dealGetIp(msg);
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
        //myId + senderId 来唯一确定聊天窗口
        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());
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

    public void dealReceiveFile(Message msg) {

        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());
        chat.printOnScreen(msg.getSendTime() + "\n正在接收来自 " + msg.getSender() + "的文件\n", Color.black, 16);
        FileTransfer.receiveFile(msg);
        chat.printOnScreen("传输完成", Color.black, 16);
    }

    public void dealGetIp(Message msg) {

        FileTransfer.sendFile(msg);
        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());
        chat.printOnScreen("传输完成", Color.black, 16);
    }
}
