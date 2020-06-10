package com.client.model;

import com.client.tools.ManageClientThread;
import com.common.User;

public class ClientUser {

    private ClientConServer clientConServer = new ClientConServer();

    public boolean checkUser (User user) {

        return clientConServer.sendLogIfoToServer(user);
    }

    public void signOut (User user) {
        clientConServer.sendOutInfo(user);
        ClientConServerThread clientConServerThread = ManageClientThread.getManageClientThread(user.getName());
        clientConServerThread.interrupt();
    }
}
