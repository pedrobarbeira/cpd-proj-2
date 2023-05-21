package org.cpd.server.service;

public class NumberGuessingGame {

    private class GameOverException extends Exception{}
    private class GameNotOverException extends Exception{}


    private final int[] plays;

    private final int nPlayers;

    private int currentPlayer;

    private final int solution;

    private final int range;

    public NumberGuessingGame(int nPlayers,int solution,int range){
        this.solution = solution;
        this.nPlayers = nPlayers;
        this.range = range;
        this.currentPlayer = 0;
        this.plays = new int[nPlayers];
    }

    public boolean isGameOver(){
        return currentPlayer >= nPlayers;
    }

    public void nextPlay(int guess) throws GameOverException{
        if (isGameOver()){
            throw new GameOverException();
        }
        plays[currentPlayer] = guess;
        currentPlayer++;
    }

    public int[] getResults() throws  GameNotOverException{
        if(!isGameOver()){
            throw new GameNotOverException();
        }
        var res = new int[nPlayers];
        for(int i =0; i < nPlayers; i++){
            int points = range - (solution - plays[i]);
            res[i] = points;
        }
        return res;
    }
}
