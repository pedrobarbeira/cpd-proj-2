package org.cpd.server.service;

import org.cpd.server.controller.GameController;
import org.cpd.shared.Pair;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Runnable{

    private final List<Pair<Integer, SocketChannel>> playerList;

    public Game(List<Pair<Integer, SocketChannel>> playerList){
        this.playerList = playerList;
    }

    @Override
    public void run() {
        int maxPoints = 0;
        int winnerId = 0;
        for(Pair<Integer, SocketChannel> pair : playerList){
            SocketChannel socket = pair.second;
            try {
                String move = GameController.sendTurn(socket, pair.first);
                if (move.length() > maxPoints) {
                    maxPoints = move.length();
                    winnerId = pair.first;
                }
            }catch(Exception e){
                System.out.println("Something went wrong");
            }
        }
        for(Pair<Integer, SocketChannel> pair : playerList){
            if(pair.first != winnerId){
                try {
                    GameController.notifyLoser(pair.second, pair.first);
                }catch(Exception e){
                    System.out.println("Something went wrong");
                }
            }else{
                try {
                    GameController.notifyWinner(pair.second, pair.first);
                }catch(Exception e){
                    System.out.println("Something went wrong");
                }
            }
        }

    }

}
