package com.client.view;

import com.client.tools.MulticastReadThread;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;


public class GroupChat extends JFrame{

    private String srcId;
    private static final String  multicastIp = "224.0.0.5";
    private static final int port = 9901;
    private static final String TERMINATE = "exit";
    private static volatile boolean finished = false;

    private InetAddress groupIp;
    private MulticastSocket multicastSocket;

    private JTextPane textOutput;
    private JTextPane textInput;
    private JButton expressionButton, sendButton, fileTransferButton;
    private JPanel jPanel;
    private JScrollPane outScroll;

//    public static void main(String[] args) {
//
//        GroupChat groupChat = new GroupChat("1");
//    }

    public GroupChat(String srcId) {
        this.srcId = srcId;
        init(srcId);
    }

    public void showMsg(String msg) {

        String showMsg = new Date().toString() + "\n" +msg + "\n";
        printOnScreen(showMsg, Color.black, 16);
    }

    public static boolean isFinished() {
        return finished;
    }


    public void dealGroupSend() {

        String messsage = textInput.getText();

        if (messsage.equalsIgnoreCase("exit")) {
            finished = true;
            try {
                multicastSocket.leaveGroup(groupIp);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            multicastSocket.close();
            System.exit(0);
        }

        String msg = srcId + ": " + messsage;
        byte[] buffer = msg.getBytes();
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, groupIp, port);

        try {
            multicastSocket.send(datagram);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        textInput.setText("");
        String myShow = new Date().toString() + "\n" + "我说； " + messsage + "\n";
        printOnScreen(myShow, Color.black, 16);

    }


    public void init(String srcId) {

        this.setLayout(null);

        textOutput = new JTextPane();
        textInput = new JTextPane();
        textInput.requestFocus();
        expressionButton = new JButton("表情");
        expressionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("fuck");
            }
        });
        sendButton = new JButton("发送");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dealGroupSend();
            }
        });

        fileTransferButton = new JButton("发送文件");

        textOutput.setEditable(false);

        jPanel = new JPanel(new FlowLayout());
        jPanel.add(expressionButton);
        jPanel.add(fileTransferButton);
        jPanel.add(sendButton);

        jPanel.setBounds(5, 350, 450, 40);

        outScroll = new JScrollPane(textOutput);
        outScroll.setBounds(5,5, 450, 200);

        textInput.setBounds(5, 230,420, 100);

        //初始化组播socket
        try {
            this.groupIp = InetAddress.getByName(multicastIp);
            multicastSocket = new MulticastSocket(port);

            multicastSocket.setTimeToLive(1);
            multicastSocket.joinGroup(groupIp);

            Thread thread = new Thread(new MulticastReadThread(srcId, multicastSocket, groupIp, port));

            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.add(outScroll);
        this.add(textInput);
        this.add(jPanel);

        this.setIconImage((new ImageIcon(this.getClass().getResource("/image/qq.gif"))).getImage());
        this.setTitle(srcId + "参与群聊");
        this.setBounds(700, 300, 470, 440);
        this.setAlwaysOnTop(true);
//      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void printOnScreen(String str, Color color, int fontSize) {
        SimpleAttributeSet attrSet = new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet, color);
        StyleConstants.setFontSize(attrSet, fontSize);

        insert(str, attrSet);
    }

    public void insert(String str, AttributeSet attrSet) {

        Document doc = textOutput.getDocument();
        try {
            doc.insertString(doc.getLength(), str, attrSet);
        }
        catch (BadLocationException e) {
            System.out.println("BadLocationException: " + e);
        }
    }
}
