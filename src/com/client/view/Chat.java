package com.client.view;

import com.client.model.ClientConServer;
import com.client.tools.ManageChat;
import com.client.tools.ManageClientThread;
import com.client.tools.Reg;
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

    private String filePath = null;
    private String chatCon = null;

    public static void main(String[] args) {

        Chat chat = new Chat("ycd", ":1");
    }

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
                Emoji emoji = new Emoji(textInput, srcId + distId);

            }
        });
        sendButton = new JButton("发送");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                chatCon = textInput.getText();
//                System.out.println(chatCon);

                if (chatCon != null) {
                    ClientConServer.dealSendAction(srcId, distId, MessageType.messageCommMsg, chatCon);
                } else {
                    chatCon = textInput.getText();
                    ClientConServer.dealSendAction(srcId, distId, MessageType.messageCommMsg, chatCon);
                }

                String myShow = new Date().toString() + "\n" + "我说: " + chatCon + "\n";

                Reg.showAll(chatCon, textOutput, srcId+distId);
                chatCon = null;
                textInput.setText("");
            }
        });

        fileTransferButton = new JButton("发送文件");
        fileTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String fileName = null;
                //按钮点击事件
                JFileChooser chooser = new JFileChooser();             //设置选择器
                chooser.setMultiSelectionEnabled(true);             //设为多选
                int returnVal = chooser.showOpenDialog(fileTransferButton);        //是否打开文件选择框
                System.out.println("returnVal="+returnVal);

                if (returnVal == JFileChooser.APPROVE_OPTION) {          //如果符合文件类型

                    //获取绝对路径
                    filePath = chooser.getSelectedFile().getAbsolutePath();
                    fileName = chooser.getSelectedFile().getName();

                    System.out.println("You chose this file: "+ chooser.getSelectedFile()
                            .getAbsolutePath());  //输出相对路径

                }

                printOnScreen(new Date().toString() +"\n" + distId + " 发送文件 "
                        + fileName + "\n", Color.black, 16);

                ClientConServer.dealSendAction(srcId, distId, MessageType.messageGetIp, fileName);


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

            Chat chat = ManageChat.getChat(msg.getGetter() + msg.getSender());
            Reg.showReceive(msg ,chat.getTextOutput(), chat);
//            this.area.append(info);

//            printOnScreen(info, Color.black, 16);
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

    public String getFilePath() {
        return filePath;
    }

    public void setChatCon(String chatCon) {
        this.chatCon = chatCon;
    }

    public JTextPane getTextOutput() {
        return textOutput;
    }
}
