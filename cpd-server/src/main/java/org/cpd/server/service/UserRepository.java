package org.cpd.server.service;

import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class UserRepository {
    private final URL dataUrl = getClass().getClassLoader().getResource(DataConstants.FILE_PATH);
    private int count;
    //TODO concurrency control here
    Map<Integer, User> userData;


    public UserRepository() {
        count = 0;
        userData = new HashMap<>();
        try {
            assert dataUrl != null;
            File dir = new File(dataUrl.toURI());
            for(File file : Objects.requireNonNull(dir.listFiles())) {
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
            }
        }catch(URISyntaxException | IOException e){
            e.printStackTrace();
        }
    }

    public Collection<User> getAll(){
        return userData.values();
    }
    
    public int getNextId(){
        return this.count+1;
    }

    public int getCount(){
        return this.count;
    }

    public String getUserTokenById(int id){
        User user = userData.get(id);
        return user.getToken();
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

    //TODO fix persistence
    public boolean addUser(User user){
        User nameCheck = getByName(user.getName());
        if(nameCheck == null) {
            this.count++;
            this.userData.put(this.count, user);
            try{
                if(save(user)){
                    String msg =String.format("User [%s] data successfully saved", user.getName());
                    System.out.println(msg);
                    return true;
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        String msg = String.format("Could not register user [%s]", user.getName());
        System.out.println(msg);
        return false;
    }

    public boolean saveById(int id){
        User user = getById(id);
        try{
            return save(user);
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }


    private boolean save(User user) throws IOException {
        assert dataUrl != null;
        String filePath = String.format("%s%d", DataConstants.FILE_BASE, count);
        File userFile = new File(filePath);
        if (!userFile.exists()) {
            if(userFile.createNewFile()) {
                FileWriter writer = new FileWriter(userFile);
                String data = getPersistenceData(user);
                writer.write(data);
                return true;
            }
        }
        String msg = String.format("Could not save user [%s]", user.getName());
        System.out.println(msg);
        return false;
    }

    public String getPersistenceData(User user){
        return String.format("%d,%s,%s,%d", user.getId(), user.getName(), user.getPassword(), user.getRank().getValue());
    }

    private static class DataConstants{
        public static final int ID = 0;
        public static final int NAME = 1;
        public static final int PASSWORD = 2;
        public static final int RANK = 3;
        public static final String FILE_PATH = "user-data/";
        public static final String FILE_BASE = "cpd-server/src/main/resources/user-data/user-";
    }
}
