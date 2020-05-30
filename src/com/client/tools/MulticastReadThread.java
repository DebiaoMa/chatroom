package com.client.tools;

import com.client.model.Multicast;
import com.client.view.GroupChat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReadThread implements Runnable{

    private static final int MAX_LEN = 1000;

    private MulticastSocket socket;
    private InetAddress group;
    private int port;
    private String srcId;

    public MulticastReadThread(String srcId, MulticastSocket socket, InetAddress group, int port) {
        this.srcId = srcId;
        this.socket = socket;
        this.group = group;
        this.port = port;
    }

    @Override
    public void run() {

        while(!GroupChat.isFinished())
        {
            byte[] buffer = new byte[MulticastReadThread.MAX_LEN];
            DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group,port);
            String message;

            try
            {
                socket.receive(datagram);
                message = new String(buffer,0,datagram.getLength(),"UTF-8");

                System.out.println(message);

                //message显示到所有群组成员的聊天界面
                if (!message.startsWith(srcId)) {
                    GroupChat groupChat = ManageChat.getGroupChat(srcId);
                    groupChat.showMsg(message);
                }
            }
            catch(IOException e)
            {
                System.out.println("Socket closed!");
            }
        }
    }

}
