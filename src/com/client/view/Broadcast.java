package com.client.view;

import com.client.tools.ManageClientThread;
import com.common.Message;
import com.common.MessageType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.Date;

public class Broadcast extends JFrame implements ActionListener {

    private String srcId;
    private String distId;

    private JTextArea area;
    private JTextField field;
    private JButton sendButton;
    private JPanel jPanel;

//    public static void main(String[] args) {
//        new Broadcast("1", "2");
//    }


    public Broadcast(String srcId, String distId) {
        this.srcId = srcId;
        this.distId = distId;

        area = new JTextArea();
        field = new JTextField(15);
        sendButton = new JButton("发送");
        sendButton.addActionListener( this);

        area.setEnabled(false);
        field.requestFocus();

        jPanel = new JPanel();
        jPanel.add(field);
        jPanel.add(sendButton);

        this.add(area, "Center");
        this.add(jPanel, "South");
        this.setIconImage((new ImageIcon(this.getClass().getResource("/image/qq.gif"))).getImage());
        this.setTitle(srcId + " 正在广播 ");
        this.setBounds(700, 300, 450, 300);
        this.setAlwaysOnTop(true);
//      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == sendButton) {

            Message msg = new Message();
            msg.setMesType(MessageType.messageBroadcast);
            msg.setSender(this.srcId);
            msg.setGetter("all");
            msg.setCon(this.field.getText());
            msg.setSendTime(new Date().toString());

            try {
                ObjectOutputStream oos = new ObjectOutputStream(
                        ManageClientThread.getManageClientThread(srcId)
                                .getSocket().getOutputStream());
                oos.writeObject(msg);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            this.field.setText("");
            String myShow = "我的广播：\n" + msg.getCon() + "\n";
            this.area.append(myShow);
        }

    }
}
