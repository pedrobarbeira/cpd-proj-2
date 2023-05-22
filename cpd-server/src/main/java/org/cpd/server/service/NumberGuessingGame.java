package org.cpd.server.service;

import java.util.Arrays;

public class NumberGuessingGame {


    private class GameOverException extends Exception {
    }

    private class GameNotOverException extends Exception {
    }


    private final int[] plays;
    private int[] points;
    private int[] sortedPoints;

    private final int nPlayers;

    private int currentPlayer;

    private final int solution;

    private final int range;


    public NumberGuessingGame(int nPlayers, int solution, int range) {
        this.solution = solution;
        this.nPlayers = nPlayers;
        this.range = range;
        this.currentPlayer = 0;
        this.plays = new int[nPlayers];
    }

    public boolean isGameOver() {
        return currentPlayer >= nPlayers;
    }

    public void nextRound(int guess) throws GameOverException {
        if (isGameOver()) {
            throw new GameOverException();
        }
        plays[currentPlayer] = guess;
        currentPlayer++;
        if (isGameOver()) {
            endGame();
        }
    }

    public void endGame() {
        createResults();
        sortedPoints = Arrays.copyOf(points, nPlayers);
        Arrays.sort(sortedPoints);
    }

    public void createResults() {
        var res = new int[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            int points = range - (solution - plays[i]);
            res[i] = points;
        }
        this.points = res;

    }

    public int[] getResults() throws GameNotOverException {
        if (!isGameOver()) {
            throw new GameNotOverException();
        }
        return points;
    }

    public String greet(int c) {
        return "Hello You Are Player nº" + c + " this is a turn based number guessing game, this party has " + nPlayers + "players the game is about to start";
    }

    public String update() {
        return "It's currently player's" + currentPlayer + " turn, waiting for a guess";
    }

    public String showResults(int c) {
        int playerPoints = plays[c];
        int place = 1;
        for (int i = 0; i < nPlayers; i++) {
            if (playerPoints == sortedPoints[i]) {
                place += i;
                break;
            }
        }
        String personal;
        if (place == 1) {
            personal = "Congratulations you finished in first place!\n";
        } else {
            personal = "You finished in " + place + " place!\n";
        }
        var res = new StringBuilder(personal);
        for (int i = 0; i < nPlayers; i++) {
            res.append("Player ").append(i).append(": ").append(points[i]).append("\n");
        }
        return res.toString();
    }

    public int[] getPoints() {
        return points;
    }
}
