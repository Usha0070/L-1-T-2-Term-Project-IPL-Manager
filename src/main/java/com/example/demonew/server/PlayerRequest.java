package com.example.demonew.server;

import java.io.Serializable;

public class PlayerRequest implements Serializable {
    private String request;
    private Player player;

    public PlayerRequest(String request, Player player) {
        this.request = request;
        this.player = player;
    }

    public PlayerRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public Player getPlayer() {
        return player;
    }
}
