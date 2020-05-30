package com.client.model;


import com.client.tools.MulticastReadThread;
import com.client.view.GroupChat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Multicast {

    private String srcId;
    private static final String  multicastIp = "224.0.0.5";
    private static final int port = 9901;
    private static final String TERMINATE = "Exit";
    private static volatile boolean finished = false;

    private InetAddress groupIp;
    private MulticastSocket multicastSocket;

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        String id;
//
//        id = scanner.nextLine();
//
//        new Multicast(id);
//    }

    public Multicast(String srcId) {

        Scanner scanner = new Scanner(System.in);

        try {
            this.groupIp = InetAddress.getByName(multicastIp);

            MulticastSocket socket = new MulticastSocket(port);

            socket.setTimeToLive(1);
            socket.joinGroup(groupIp);

            Thread thread = new Thread(new MulticastReadThread(srcId, socket, groupIp, port));

            thread.start();

            // sent to the current group
            System.out.println("Start typing messages...\n");
            while(true)
            {
                String message;
                message = scanner.nextLine();
                if(message.equalsIgnoreCase(Multicast.TERMINATE))
                {
                    finished = true;
                    socket.leaveGroup(groupIp);
                    socket.close();
                    break;
                }
                message = srcId + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket datagram = new
                        DatagramPacket(buffer, buffer.length, groupIp, port);
                socket.send(datagram);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFinished() {
        return finished;
    }
}
