package com.client.model;

import com.client.tools.ManageChat;
import com.client.tools.ManageFriendList;
import com.client.view.Chat;
import com.common.Message;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;

public class FileTransfer {

    public static void sendFile(Message msg) {
        Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());

        String file = chat.getFilePath();

        try {
            DatagramSocket datagramSocket = new DatagramSocket((int) (Math.random()*1000 + 8000));

            byte[] buffer = FileUtils.readFileToByteArray(new File(file));

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 1; i < msg.getCon().length(); i++) {
                stringBuffer.append(msg.getCon().charAt(i));
            }

            String ip = stringBuffer.toString();

            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length,
                    new InetSocketAddress(ip, 6666));

            datagramSocket.send(datagramPacket);

            datagramSocket.close();

            System.out.println("I sent it");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void receiveFile(Message msg) {

        String fileName = msg.getCon();

        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket(6666);

            byte[] buffer = new byte[1024 * 8];

            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);

            datagramSocket.receive(datagramPacket);

            //获得当前用户工作路径
            String usrHome = System.getProperty("user.home");
            String path = usrHome + "\\Downloads\\" + fileName;


            FileUtils.writeByteArrayToFile(new File(path), datagramPacket.getData());

            System.out.println(FileUtils.readFileToString(new File(path), "UTF-8"));

            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
