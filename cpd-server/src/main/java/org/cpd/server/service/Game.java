package org.cpd.server.service;

import org.cpd.server.controller.GameController;
import org.cpd.shared.Pair;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game implements Runnable {

    private final List<Pair<Integer, SocketChannel>> playerList;

    private final NumberGuessingGame game;

    public Game(List<Pair<Integer, SocketChannel>> playerList) {
        this.playerList = List.copyOf(playerList);
        this.game = new NumberGuessingGame(playerList.size(), 3, 10);

    }

    private void setUp() throws Exception {
        int c = 0;
        for (var pair : playerList) {
            SocketChannel s = pair.second;
            GameController.sendMessage(s, pair.first, game.greet(c));
            c++;
        }

    }

    private void update() throws Exception {
        for (var pair : playerList) {
            GameController.sendMessage(pair.second, pair.first, game.update());
        }
    }

    private void loop() throws Exception {

        while (!game.isGameOver()) {
            for (var pair : playerList) {
                update();
                SocketChannel socket = pair.second;
                try {
                    String move = GameController.sendTurn(socket, pair.first);
                    int guess;
                    try {
                        guess = Integer.parseInt(move);
                    } catch (NumberFormatException e) {
                        guess = 0;
                    }
                    game.nextRound(guess);
                } catch (Exception e) {
                    System.out.println("Something went wrong");
                }
            }
        }
    }

    private void tearDown() throws  Exception{
        int c = 0;
        for (var pair : playerList) {
            GameController.sendMessage(pair.second, pair.first, game.showResults(c));
            c++;
        }
    }

    @Override
    public void run() {
        try {
            setUp();
            loop();
            tearDown();
        } catch (Exception e) {
            System.err.println("Something Went Wrong while running the game");
            e.printStackTrace();
        }
    }

}
