package org.cpd.shared;

public class User {
    private static final String DELIMITER = ",";
    private final int id;
    private final Credentials credentials;
    private final Rank rank;
    private String token;



    public User (int id, String name, String password){
        this(id, name, password, null, null);
    }

    public User(int id, String name, String password, Integer rank) {
        this(id, name, password, rank, null);
    }

    public User(int id, String name, String password, Integer rank, String token){
        this.id = id;
        this.credentials = new Credentials(name, password);
        if(rank != null) {
            this.rank = new Rank(rank);
        }else{
            this.rank = new Rank();
        }
        this.token = token;
    }

    public String serialize(){
        String idString = String.valueOf(id);
        String rankString = String.valueOf(rank.getValue());
        return String.join(DELIMITER, idString,
                credentials.getName(), credentials.getPassword(), rankString, token);
    }

    public static User deserialize(String str){
        String[] data = str.split(DELIMITER);
        int id = Integer.parseInt(data[DataIndex.ID]);
        int rank = Integer.parseInt(data[DataIndex.RANK]);
        return new User(id, data[DataIndex.NAME], data[DataIndex.PASSWORD], rank, data[DataIndex.TOKEN]);
    }

    public Rank getRank(){
        return this.rank;
    }

    public String getName(){
        return this.credentials.getName();
    }

    public String getPassword(){ return this.credentials.getPassword();}

    public int getId(){return this.id;}

    public String getToken(){ return this.token;}

    public void setToken(String token){
        this.token = token;
    }

    public void updateRank(int rank){
        this.rank.add(rank);
    }

    public boolean validate(String password){
        return this.credentials.validate(password);
    }

    private static class DataIndex{
        public static final int ID = 0;
        public static final int NAME = 1;
        public static final int PASSWORD = 2;
        public static final int RANK = 3;
        public static final int TOKEN = 4;
    }
}
