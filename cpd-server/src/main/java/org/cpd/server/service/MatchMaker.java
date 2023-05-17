package org.cpd.server.service;

import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

//TODO concurrency control
public class MatchMaker {
    public static final String WINNER = "The winner is [%s]\n";
    private static final int RANK_WIN = 10;
    private static final int RANK_LOSS = 10;
    private final UserRepository userRepository;
    public static enum Type{
        SIMPLE, RANK
    }

    private final Type matchType;
    private Map<Integer, Socket> playerSockets;

    private final int numPlayers;
    private int playerCount;
    private int rank;

    public MatchMaker(int numPlayers, Type type, UserRepository userRepository){
        this.matchType = type;
        this.playerSockets = new HashMap<>();
        this.numPlayers = numPlayers;
        this.playerCount = 0;
        this.rank = 0;
        this.userRepository = userRepository;
    }

    public int getCount(){
        return this.playerCount;
    }

    public int getRank(){
        return this.rank;
    }

    public String startMatch(){
        if(playerCount != numPlayers){
            throw new RuntimeException("Not enough players to start match");
        }
        Game game = new Game(this.numPlayers, this.playerSockets);
        User winner = game.start();
        if(this.matchType.equals(Type.RANK)) {
            winner.updateRank(RANK_WIN);
            for (Integer id : playerSockets.keySet()) {
                User user = userRepository.getById(id);
                user.updateRank(RANK_LOSS);
            }
        }
        return String.format(WINNER, winner.getName());
    }

    public boolean addPlayer(User player, Socket socket, int rankOffset){
        if(this.playerCount >= this.numPlayers) return false;
        if(this.matchType.equals(Type.RANK)){
            Rank rank = new Rank(this.rank, rankOffset);
            Rank playerRank = player.getRank();
            if(!rank.isInBounds(playerRank.getValue())) return false;
        }
        return addPlayer(player, socket);
    }

    private boolean addPlayer(User player, Socket socket){
        this.playerCount++;
        this.playerSockets.put(player.getId(), socket);
        int tmpRank = 0;
        for(Integer id : playerSockets.keySet()){
            User user = userRepository.getById(id);
            Rank rank = user.getRank();
            tmpRank += rank.getValue();
        }
        this.rank = tmpRank / this.playerCount;
        return true;
    }
}
