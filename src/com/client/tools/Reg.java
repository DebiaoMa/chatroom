package com.client.tools;

import com.client.view.Chat;
import com.common.Message;


import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Date;


public class Reg extends JFrame{

    JTextPane jTextPane;

    public Reg(){
        this.setLayout(null);
        jTextPane = new JTextPane();
        jTextPane.setBounds(5,5,100,100);

        this.setVisible(true);
        this.setBounds(100,100, 150, 150);
        this.add(jTextPane);
    }

//    public static void main(String[] args) {
//
//        Reg reg = new Reg();
//
//        showAll("哈哈哈[smile]",reg.jTextPane, reg);
//    }

    public static void showAll(String chatCon, JTextPane textOutPane, String chatId) {

        char[] content = chatCon.toCharArray();
        System.out.println(chatCon);

        Chat chat = ManageChat.getChat(chatId);

        String myShow = new Date().toString() + "\n" + "我说: ";
        chat.printOnScreen(myShow, Color.black, 16);

        if (!chatCon.contains("[")) {
            chat.printOnScreen(chatCon + "\n", Color.black, 16);
            return;
        }

        int inter = 0;
        while(inter < content.length) {
            for (int i = 0; i < content.length; i++) {
                if (content[i] == '[') {
                    inter = i;
                    break;
                } else {
                    chat.printOnScreen(String.valueOf(content[i]), Color.black, 16);
                }
            }
            StringBuffer buffer = new StringBuffer();
            for (int i = inter+1; i < content.length; i++) {
                if (content[i] == ']') {
                    inter = i+1;
                    break;
                } else {
                    buffer.append(content[i]);
                }
            }

            getImage(textOutPane, buffer.toString());

        }
            chat.printOnScreen("\n" , Color.black, 16);
    }

//    public static void showAll(String chatCon, JTextPane textOutPane, Reg reg) {
//
//        char[] content = chatCon.toCharArray();
//        System.out.println(chatCon);
//
//        int inter = 0;
//        while(inter < content.length) {
//            for (int i = 0; i < content.length; i++) {
//                if (content[i] == '[') {
//                    inter = i;
//                    break;
//                } else {
//                    reg.printOnScreen(String.valueOf(content[i]), Color.black, 16);
//                    System.out.printf(String.valueOf(content[i]));
//                }
//            }
//            StringBuffer buffer = new StringBuffer();
//            for (int i = inter + 1; i < content.length; i++) {
//                if (content[i] == ']') {
//                    inter = i + 1;
//                    break;
//                } else {
//                    buffer.append(content[i]);
//                }
//            }
//            System.out.println(buffer.toString());
//            getImage(textOutPane, buffer.toString());
//        }
//
//    }


    public static void showReceive(Message msg, JTextPane OutPane, Chat chat) {
        String info = msg.getSendTime() + " \n" + msg.getSender() + " 对你说： ";
        chat.printOnScreen(info, Color.black, 16);

        if (!msg.getCon().contains("[")) {
            chat.printOnScreen(msg.getCon() + "\n", Color.black, 16);
            return;
        }

        char[] content = msg.getCon().toCharArray();

        int inter = 0;
        while(inter < content.length) {
            for (int i = 0; i < content.length; i++) {
                if (content[i] == '[') {
                    inter = i;
                    break;
                } else {
                    chat.printOnScreen(String.valueOf(content[i]), Color.black, 16);
                }
            }
            StringBuffer buffer = new StringBuffer();
            for (int i = inter + 1; i < content.length; i++) {
                if (content[i] == ']') {
                    inter = i + 1;
                    break;
                } else {
                    buffer.append(content[i]);
                }
            }

            getImage(OutPane, buffer.toString());

            chat.printOnScreen("\n" , Color.black, 16);
        }
    }

    private static void getImage(JTextPane OutPane, String ImageName) {

        StyledDocument doc = OutPane.getStyledDocument();
        OutPane.setCaretPosition(doc.getLength());
        OutPane.insertIcon(new ImageIcon(System.getProperty("user.dir") + "\\emoji\\" + ImageName + ".png"));

    }

//    private static void getImage(JTextPane OutPane, String ImageName) {
//
//        StyledDocument doc = OutPane.getStyledDocument();
//        OutPane.setCaretPosition(doc.getLength());
//        OutPane.insertIcon(new ImageIcon(System.getProperty("user.dir") + "\\emoji\\" + ImageName + ".png"));
//    }
//
//    public void printOnScreen(String str,Color color, int fontSize) {
//        SimpleAttributeSet attrSet = new SimpleAttributeSet();
//        StyleConstants.setForeground(attrSet, color);
//        StyleConstants.setFontSize(attrSet, fontSize);
//
//        insert(str, attrSet);
//    }
//
//    public void insert(String str, AttributeSet attrSet) {
//
//        Document doc = jTextPane.getDocument();
//        try {
//            doc.insertString(doc.getLength(), str, attrSet);
//        }
//        catch (BadLocationException e) {
//            System.out.println("BadLocationException: " + e);
//        }
//    }
}
