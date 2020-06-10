package com.client.model;


import com.client.tools.ManageClientThread;
import com.common.Message;
import com.common.MessageType;
import com.common.User;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientConServer {


    public boolean sendLogIfoToServer (Object o) {

        boolean flag = false;

        try {
            Socket socket = new Socket("192.168.0.103", 9999);
            //发送验证消息
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(o);

            //服务器返回的登陆msg流
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message  msg = (Message) ois.readObject();

            if (msg.getMesType().equals("1")) {

                ClientConServerThread ccst = new ClientConServerThread(socket);
                ccst.start();
                ManageClientThread.addManageClientThread(((User)o).getName(), ccst);

                flag = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static void dealSendAction(String srcId, String distId,String messageType, String content) {

        Message msg = new Message();
        msg.setMesType(messageType);
        msg.setSender(srcId);
        msg.setGetter(distId);
        msg.setCon(content);
        msg.setSendTime(new Date().toString());

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientThread.getManageClientThread(srcId).getSocket().getOutputStream());
            oos.writeObject(msg);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendOutInfo(User user) {

        dealSendAction(user.getName(), "server", MessageType.messageOut, user.getName() + "登出");

    }

}
