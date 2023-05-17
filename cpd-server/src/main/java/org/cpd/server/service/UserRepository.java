package org.cpd.server.service;

import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class UserRepository {
    private final URL dataUrl = getClass().getClassLoader().getResource("users.csv");
    private int count;
    //TODO concurrency control here
    Map<Integer, User> userData;


    public UserRepository() {
        count = 0;
        userData = new HashMap<>();
        try {
            File file = new File(dataUrl.toURI());
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                int id = Integer.parseInt(data[DataConstants.ID]);
                User user = new User(id, data[DataConstants.NAME],
                        data[DataConstants.PASSWORD],
                        Integer.parseInt(data[DataConstants.RANK]));
                userData.put(id, user);
                count++;
            }
            scanner.close();
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }
    
    public int getNextId(){
        return this.count+1;
    }

    public int getCount(){
        return this.count;
    }
    
    public User getById(int id){
        return userData.get(id);
    }

    public User getByName(String name){
        for(User user : userData.values()){
            if(user.getName().equals(name)){
                return user;
            }
        }
        return null;
    }

    public List<User> getByRank(int rank){
        List<User> toReturn = new ArrayList<>();
        for(User user : userData.values()){
            Rank userRank = user.getRank();
            if(userRank.isInBounds(rank)){
                toReturn.add(user);
            }
        }
        return toReturn;
    }

    public boolean addUser(User user){
        User nameCheck = getByName(user.getName());
        if(nameCheck == null) {
            this.count++;
            this.userData.put(this.count, user);
            return true;
        }
        return false;
    }


    public void save() {
        try{
            File file = new File(dataUrl.toURI());
            FileWriter out = new FileWriter(file);
            String format = "%s,%s,%s,%s\n";
            for(User user : userData.values()){
                Rank rank = user.getRank();
                String data = String.format(format, user.getId(), user.getName(), user.getPassword(), rank.getValue());
                out.write(data);
            }
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }

    private static class DataConstants{
        public static final int ID = 0;
        public static final int NAME = 1;
        public static final int PASSWORD = 2;
        public static final int RANK = 3;
        public static final String FILE_PATH = "users.csv";
    }
}
