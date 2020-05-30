package com.client.model;

import com.common.User;

public class ClientUser {

    public boolean checkUser (User user) {

        return new ClientConServer().sendLogIfoToServer(user);
    }
}
