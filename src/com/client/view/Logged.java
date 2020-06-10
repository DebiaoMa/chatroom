package com.client.view;

import com.client.tools.ManageChat;
import com.common.Message;
import com.common.MessageType;
import com.common.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 包括陌生人和黑名单
 */
public class Logged extends JFrame implements ActionListener, MouseListener {

    private String srcId;

    //处理第一张卡片，点击我的好友时的界面
    JPanel friendFrame, friendList;
    JButton friend, group, broadcast, outButton;
    JScrollPane scrollList;   //滚动列表

    //处理第二张，点击卡片获得群聊时的界面
//    JPanel groupFrame, groupList, total;
//    JButton friendNull, group;
//    JScrollPane scrollListGroup;   //滚动列表

    //给friendList先初始化21个好友
    JLabel[] eachFriend = new JLabel[21];
    //模拟20个群组
//    JLabel[] eachGroup = new JLabel[20];

    CardLayout cardLayout;

    public static void main(String[] args) {

        Logged logged = new Logged("1");
    }


    public Logged(String srcId) {

        this.srcId = srcId;

        //处理第一张卡片，显示好友列表
        friend = new JButton("我的好友");
        //先不添加
        group = new JButton("我的群组");
        group.addActionListener(this);
        broadcast = new JButton("我要广播");
        broadcast.addActionListener(this);
        outButton = new JButton("退出登录");
        outButton.addActionListener(this);

        friendFrame = new JPanel(new BorderLayout());
        //假装有21个好友
        friendList = new JPanel(new GridLayout(21, 1, 4, 4));


        for (int i = 0; i < eachFriend.length; i++) {

            eachFriend[i] = new JLabel((i+1) + "", new ImageIcon(this.getClass().getResource("/image/mm.jpg")), JLabel.LEFT);
            //默认所有用户都不在线，除当前账号外
            eachFriend[i].setEnabled(false);

            if (eachFriend[i].getText().equals(srcId)) {
                eachFriend[i].setEnabled(true);
            }

            eachFriend[i].addMouseListener(this);
            friendList.add(eachFriend[i]);
        }

        scrollList = new JScrollPane(friendList);

        JPanel total = new JPanel(new GridLayout(3,1));
        total.add(group);
        total.add(broadcast);
        total.add(outButton);

        friendFrame.add(friend, "North");
        friendFrame.add(scrollList, "Center");
        friendFrame.add(total, "South");

        //处理第二张卡片
//        friendNull = new JButton("我的好友");
//        friendNull.addActionListener(this);
//        group = new JButton("我的群组");
//
//        groupFrame = new JPanel(new BorderLayout());
//        //假装有20个群组
//        groupList = new JPanel(new GridLayout(20, 1, 4, 4));
//
//
//        for (int i = 0; i < eachGroup.length; i++) {
//            eachGroup[i] = new JLabel(i+1 + "", new ImageIcon(this.getClass().getResource("/image/qq.gif")), JLabel.LEFT);
//            eachGroup[i].addMouseListener(this);
//            groupList.add(eachGroup[i]);
//        }
//
//        total = new JPanel(new GridLayout(2,1));
//        total.add(friendNull);
//        total.add(group);
//
//        scrollListGroup = new JScrollPane(groupList);
//
//        groupFrame.add(total, "North");
//        groupFrame.add(scrollListGroup, "Center");
//
        cardLayout = new CardLayout();

        this.setLayout(cardLayout);

        this.add(friendFrame, "card1");
//        this.add(groupFrame, "card2");

        this.setBounds(300,300,250,470);
        //显示自己的账户
        this.setTitle(this.srcId);
        this.setIconImage((new ImageIcon(this.getClass().getResource("/image/qq.gif"))).getImage());

//        this.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//
//                dispose();
//            }
//
//            @Override
//            public void windowClosed(WindowEvent e) {
//                Message msg = new Message();
//                msg.setSender(srcId);
//                msg.setGetter("server");
//                msg.setCon("sign out");
//                msg.setMesType(MessageType.messageOut);
//
//                Login.clientUser.signOut(msg);
//            }
//        });

        this.setAlwaysOnTop(true);
//        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public String getSrcId() {
        return srcId;
    }

    public void updateFriend(Message msg) {

        String onlineList = msg.getCon();

        for (int i = 1; i <= 20; i++) {
            if (onlineList.contains(String.valueOf(i))){
                eachFriend[i-1].setEnabled(true);
            } else {
                eachFriend[i-1].setEnabled(false);
            }

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //点击了群组按钮就显示第二张卡片
//        if (e.getSource() == groupNUll) {
//            cardLayout.show(this.getContentPane(), "card2");
//        } else if (e.getSource() == friendNull){
//            cardLayout.show(this.getContentPane(), "card1");
//        } else if (e.getSource() == eachGroup) {
//            System.out.println("group");
//        }

        if (e.getSource() == group) {
            GroupChat groupChat = new GroupChat(srcId);
            System.out.println(srcId);
            ManageChat.addGroupChat(srcId, groupChat);
        } else if (e.getSource() == broadcast) {
            Broadcast broadcast = new Broadcast(srcId, "allLogged");
        } else if (e.getSource() == outButton) {
            System.out.println("退出登录");
            User user = new User();
            user.setName(srcId);

            Login.clientUser.signOut(user);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        //相应双击事件，得到编号
        if (e.getClickCount() == 2) {
            String friend = ((JLabel)e.getSource()).getText();
//            System.out.println(e.getSource());
            System.out.println(friend + "聊天");
            Chat chat = new Chat(this.getSrcId(), friend);

            ManageChat.addChat(srcId + friend, chat);

        }

//        if (e.getSource() == eachFriend) {
//            if (e.getClickCount() == 2) {
//
//                String friendNo = ((JLabel)e.getSource()).getText();
//                System.out.println("你将和" + friendNo + "聊天");
//                Chat chat = new Chat(this.getSrcId(), friendNo);
//
//                ManageChat.addChat(srcId + friendNo, chat);
//            }
//        } else if (e.getSource() == eachGroup) {
//            System.out.println("group");
//        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        JLabel jLabel = (JLabel)e.getSource();
        jLabel.setForeground(Color.red);
    }

    @Override
    public void mouseExited(MouseEvent e) {

        JLabel jLabel = (JLabel)e.getSource();
        jLabel.setForeground(Color.black);
    }
}
