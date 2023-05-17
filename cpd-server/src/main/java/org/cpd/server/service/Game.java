package org.cpd.server.service;

import org.cpd.shared.User;

import java.net.Socket;
import java.util.Map;

public class Game implements Runnable{
    private Map<Integer, Socket> userSockets;
    public Game(int players, Map<Integer, Socket> userSockets) {
        if(players != userSockets.size()){
            throw new RuntimeException("Number of players doesn't match number of sockets");
        }
        this.userSockets = userSockets;
    }
    public User start() {
        System.out.println("Starting game with " + userSockets.size() + " players");
        //returns winner
        return null;
    }

    @Override
    public void run() {

    }
}
