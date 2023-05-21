package org.cpd.server.service;

import org.cpd.shared.Config;
import org.cpd.shared.Pair;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

//TODO concurrency control
public class MatchMaker {
    private static Config config;
    private static final List<Pair<Integer, SocketChannel>> match = new ArrayList<>();

    public static void config(Config configuration){
        config = configuration;
    }

    public static Game findNormalMatch(int userId, SocketChannel socket){
        match.add(new Pair<>(userId, socket));
        if(match.size() == config.maxPlayers){
            System.out.println("[LOG] Creating a game");
            Game game = new Game(match);
            match.clear();
            return game;
        }
        return null;
    }

}
