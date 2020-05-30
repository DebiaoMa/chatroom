package com.client.view;

import com.client.model.ClientConServer;
import com.client.tools.ManageClientThread;
import com.common.Message;
import com.common.MessageType;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.util.Date;


/**
 * 因为客户端处于读取状态，因此我们把它作为一个线程
 */
public class Chat extends JFrame{

    private String srcId;
    private String distId;

    private JTextPane textOutput;
    private JTextPane textInput;
    private JButton expressionButton, sendButton, fileTransferButton;
    private JPanel jPanel;
    private JScrollPane outScroll;


//    public static void main(String[] args) {
//
//        Chat chat = new Chat("ycd", ":1");
//    }

    public Chat(String srcId, String distId) {

        this.srcId = srcId;
        this.distId = distId;

        init(srcId, distId);
    }

    public void init(String srcId, String distId) {

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
                ClientConServer.dealSendAction(srcId, distId, textInput.getText());
                String myShow = new Date().toString() + "\n" + "我说； " + textInput.getText() + "\n";
                textInput.setText("");
                printOnScreen(myShow, Color.black, 16);
            }
        });
        fileTransferButton = new JButton("发送文件");
        fileTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        textOutput.setEditable(false);

        jPanel = new JPanel(new FlowLayout());
        jPanel.add(expressionButton);
        jPanel.add(fileTransferButton);
        jPanel.add(sendButton);

        jPanel.setBounds(5, 350, 450, 40);

        outScroll = new JScrollPane(textOutput);
        outScroll.setBounds(5,5, 450, 200);

        textInput.setBounds(5, 230,420, 100);

        this.add(outScroll);
        this.add(textInput);
        this.add(jPanel);

        this.setIconImage((new ImageIcon(this.getClass().getResource("/image/qq.gif"))).getImage());
        this.setTitle(srcId + " 正在和 " + distId + " 聊天");
        this.setBounds(700, 300, 470, 440);
        this.setAlwaysOnTop(true);
//      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void showMessage(Message msg) {
        if (msg.getMesType().equals(MessageType.messageCommMsg)) {
            String info = msg.getSendTime() + " \n" + msg.getSender() + " 对你说： " + msg.getCon() + "\n";
//            this.area.append(info);

            printOnScreen(info, Color.black, 16);
        } else if (msg.getMesType().equals(MessageType.messageBroadcast)) {
            String info = msg.getSendTime() + " \n" + msg.getSender() + "的广播： " + msg.getCon() + "\n";
//            this.area.append(info);

            printOnScreen(info, Color.black, 16);
        }

    }


    public void printOnScreen(String str,Color color, int fontSize) {
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
