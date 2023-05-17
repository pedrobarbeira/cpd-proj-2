package org.cpd.shared;

public class Config {
    public String host = "localhost";
    public final int serverPort = 8000;
    public final int authPort = 8080;
    public final int pingPort = 8070;
    public boolean rankedOn = true;
    public final int maxGames = 5;
    public final int rankOffset = 25;
    public final int timeout = 30;
    public final int maxPlayers = 4;

    public int userId = Constants.NO_USER;

    public void setUserId(int id){
        this.userId = id;
    }

    private static class Constants{
        public static final int NO_USER = -1;
    }
}
