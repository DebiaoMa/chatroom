package com.client.view;

import com.client.model.ClientUser;
import com.client.tools.ManageClientThread;
import com.client.tools.ManageFriendList;
import com.common.Message;
import com.common.MessageType;
import com.common.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Login extends JFrame implements ActionListener {

    private static final long serialVersionUID = 4410731225941890026L;
    //界面北部的部件
    JLabel northLable;

    //界面中部的部件
    JPanel centerJPanel;
    JLabel accountLabel, passwordLabel;
    JTextField accountField;
    JPasswordField passwordField;

    //界面南部的部件
    JPanel southJPanel;
    JButton log, cancel, guide;

    public static void main(String[] args) {

        Login login = new Login();
    }

    public Login() {
        //处理北部
        northLable = new JLabel(new ImageIcon(this.getClass().getResource("/image/tou11.gif")));

        //处理中部
        centerJPanel = new JPanel(new GridLayout(3, 2));
        accountLabel = new JLabel("账户名", JLabel.CENTER);
        passwordLabel = new JLabel("密码", JLabel.CENTER);
        accountLabel.setForeground(Color.blue);
        passwordLabel.setForeground(Color.blue);
        accountField = new JTextField();
        passwordField = new JPasswordField();

        centerJPanel.add(accountLabel);
        centerJPanel.add(accountField);
        centerJPanel.add(passwordLabel);
        centerJPanel.add(passwordField);

        //处理南部
        southJPanel = new JPanel();
        log = new JButton(new ImageIcon(this.getClass().getResource("/image/denglu.gif")));
        //响应用户的登陆
        log.addActionListener(this);

        cancel = new JButton(new ImageIcon(this.getClass().getResource("/image/quxiao.gif")));
        guide = new JButton(new ImageIcon(this.getClass().getResource("/image/xiangdao.gif")));

        //将三个按钮归位
        southJPanel.add(log);
        southJPanel.add(cancel);
        southJPanel.add(guide);

        this.add(northLable, "North");
        this.add(centerJPanel, "Center");
        this.add(southJPanel, "South");
        this.setBounds(500, 300, 350, 240);
        this.setIconImage((new ImageIcon(this.getClass().getResource("/image/qq.gif"))).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == log) {   //创建登陆实例
            ClientUser clientUser = new ClientUser();
            User user = new User();
            user.setName(accountField.getText().trim());
            user.setPassword(new String(passwordField.getPassword()));

            if (clientUser.checkUser(user)) {

                //创建好友列表提前，避免确认消息返回，却没有logged
                Logged logged = new Logged(user.getName());
                ManageFriendList.addFriendList(user.getName(), logged);

                try {
                    ObjectOutputStream oos = new ObjectOutputStream(
                            ManageClientThread.getManageClientThread(user.getName()).getSocket().getOutputStream());

                    Message msg = new Message();
                    msg.setMesType(MessageType.messageGetOnlineFriend);
                    msg.setSender(user.getName());
                    msg.setGetter("server");
                    msg.setCon(null);
                    oos.writeObject(msg);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                this.dispose();
            } else {   //跳出窗口
                JOptionPane.showMessageDialog(this, "用户名或密码错误");
            }
        }
    }

}
