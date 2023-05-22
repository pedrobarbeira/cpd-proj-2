package org.cpd.client.view;

import org.cpd.client.controller.GameController;
import org.cpd.client.controller.ServerMessage;
import org.cpd.shared.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

public class GameView {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final GameController controller;
    private final User user;

    public GameView(User user, GameController controller) {
        this.controller = controller;
        controller.registerUser(user);
        this.user = user;
    }

    public void run() {
        System.out.printf((Messages.WELCOME) + "%n", user.getName());
        System.out.printf((Messages.RANK) + "%n", user.getRank().getValue());
        System.out.printf((Messages.TOKEN) + "%n", user.getToken());
        boolean playing = true;
        while (playing) {
            System.out.println(Messages.NORMAL_MATCH);
            System.out.println(Messages.RANKED_MATCH);
            System.out.println(Messages.EXIT);
            String option;
            try {
                while (true) {
                    System.out.print(AuthView.Messages.PROMPT);
                    option = reader.readLine();
                    if (!(option.equals(Options.NORMAL)
                            || option.equals(Options.RANKED)
                            || option.equals(Options.EXIT))) {
                        System.out.println(AuthView.Messages.INVALID_OPTION);
                    } else break;
                }
                switch (option) {
                    case Options.NORMAL -> {
                        normalMatch();
                    }
                    case Options.RANKED -> {
                        rankedMatch();
                    }
                    case Options.EXIT -> {
                        exit();
                        playing = false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void normalMatch() {
        String msg = controller.findNormalMatch();
        System.out.println(msg);
        play();
    }

    private void rankedMatch() {
        String msg = controller.findRankedMatch();
        System.out.println(msg);
        play();
    }

    private void respond(){
        try {
            System.out.print("$");
            String msg = reader.readLine();
            controller.makeMove(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play() {
        ServerMessage msg = null;
        boolean gameOver = false;
        while (!gameOver) {
            msg = controller.turn();
            if(msg == null){
               continue;
            }
            gameOver = msg.isOver();
            System.out.println(msg.message());
            if(msg.doRespond()){
                respond();
            }
        }
    }


    private void exit() {
        int count = 0;
        while (count < 3) {
            if (!controller.disconnect(user)) {
                count++;
            } else break;
        }
        System.out.println(AuthView.Messages.GOODBYE);
    }

    private static class Messages {
        public static final String WELCOME = "\n\nWelcome %s";
        public static final String RANK = "Rank: %d";
        public static final String TOKEN = "Session Token: %s";
        public static final String NORMAL_MATCH = String.format("\t[%s] Normal Match", Options.NORMAL);
        public static final String RANKED_MATCH = String.format("\t[%s] Ranked Match", Options.RANKED);
        public static final String EXIT = String.format("\n\t[%s] Exit", Options.EXIT);
        public static final String YOUR_TURN = "It's your turn! Type in a message";
        public static final String YOU_WIN = "Congratulations! You win!";
        public static final String YOU_LOSE = "You've lost. So sad :(";
    }

    private static class Options {
        public static final String NORMAL = "1";
        public static final String RANKED = "2";
        public static final String EXIT = "0";

    }
}
