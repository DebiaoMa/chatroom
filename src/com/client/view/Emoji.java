package com.client.view;

import com.client.tools.ManageChat;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Emoji extends JFrame {

    JTextPane jTextPane;
    StyledDocument doc;

//    public static void main(String[] args) {
//        new Emoji();
//    }

    public Emoji(JTextPane inTextPane, String chatId) {

        StyledDocument doc = inTextPane.getStyledDocument();

        this.setLayout(new GridLayout(4,4,5,5));

        List<String> files = new ArrayList<>();
        File file = new File(System.getProperty("user.dir") + "\\emoji\\");
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {

//            System.out.println(tempList[i]);

            JButton jPhoto = new JButton();
            jPhoto.setIcon(new ImageIcon(String.valueOf(tempList[i])));
            jPhoto.setContentAreaFilled(false);
            jPhoto.setBorderPainted(false);

            String path = String.valueOf(tempList[i]);
            String photo = new File(String.valueOf(tempList[i])).getName();
            jPhoto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    inTextPane.setCaretPosition(doc.getLength());
                    inTextPane.insertIcon(new ImageIcon(path));

                    StringBuffer buffer = new StringBuffer();
                    for (int i = 0; i < inTextPane.getText().length(); i++) {
                        if (inTextPane.getStyledDocument().getCharacterElement(i).getName().equals("icon")) {
                            buffer.append("[" + photo.substring(0, photo.lastIndexOf(".")) + "]");
                            System.out.printf("[" + photo.substring(0, photo.lastIndexOf(".")) + "]");
                        } else {
                            try {
                                buffer.append(inTextPane.getStyledDocument().getText(i,1));
                                System.out.printf(inTextPane.getStyledDocument().getText(i,1));
                            } catch (BadLocationException badLocationException) {
                                badLocationException.printStackTrace();
                            }
                        }
                    }
                    System.out.println();
                    Chat chat = ManageChat.getChat(chatId);
                    chat.setChatCon(buffer.toString());
                    dispose();
                }
            });

            this.add(jPhoto);
        }

        this.setBounds(100,100,150,200);
        this.setVisible(true);
    }
}
