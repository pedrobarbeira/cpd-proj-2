package org.cpd.server.service;

import org.cpd.shared.Pair;
import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserRepository {
    private final URL dataUrl = getClass().getClassLoader().getResource(DataConstants.FILE_PATH);
    public static final ReadWriteLock dataLock = new ReentrantReadWriteLock();
    private int count;
    //TODO concurrency control here
    Map<Integer, User> userData;
    Set<Integer> loggedInUsers;


    private static UserRepository singleton = null;

    public static UserRepository get() {

        if (singleton == null) {
            try {
                dataLock.writeLock().lock();
                singleton = new UserRepository();
            } finally {
                dataLock.writeLock().unlock();
            }
        }
        return singleton;
    }


    private UserRepository() {
        loggedInUsers = new HashSet<>();
        userData = new HashMap<>();
        try {
            assert dataUrl != null;
            File dir = new File(dataUrl.toURI());
            count = Objects.requireNonNull(dir.listFiles()).length;
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] data = line.split(",");
                    int id = Integer.parseInt(data[DataConstants.ID]);
                    User user = new User(id, data[DataConstants.NAME],
                            data[DataConstants.PASSWORD],
                            Integer.parseInt(data[DataConstants.RANK]));
                    userData.put(id, user);
                }
                scanner.close();
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextId() {
        return this.count + 1;
    }

    public String getUserTokenById(int id) {
        User user = userData.get(id);
        return user.getToken();
    }

    public User getById(int id) {
        User res = null;
        try {
            dataLock.readLock().lock();
            res = userData.get(id);
        } finally {
            dataLock.readLock().unlock();
        }
        return res;
    }

    public User getByName(String name) {
        try {
            dataLock.readLock().lock();
            for (User user : userData.values()) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
        } finally {
            dataLock.readLock().unlock();
        }
        return null;
    }


    public static void updateScores(List<Pair<Integer, Integer>> results) {
        try {
            dataLock.writeLock().lock();
            var u = UserRepository.get();
            for (var res : results) {
                var userId = res.first;
                var points = res.second;
                var user = u.userData.get(userId);

                user.updateRank(points);
                u.save(user);
            }
        } finally {
            dataLock.writeLock().unlock();

        }
    }

    public static void logOutUsers(List<Integer> users) {
        try {
            dataLock.writeLock().lock();
            UserRepository u = get();
            for (var user : users) {
                u.logOut(user);
            }

        } finally {
            dataLock.writeLock().unlock();
        }
    }

    public boolean addUser(User user) {
        try {
            dataLock.writeLock().lock();
            User nameCheck = getByName(user.getName());
            if (nameCheck == null) {
                this.count++;
                this.userData.put(this.count, user);
                if (save(user)) {
                    String msg = String.format("User [%s] data successfully saved", user.getName());
                    System.out.println(msg);
                    return true;
                }
            }
        } finally {
            dataLock.writeLock().unlock();
        }
        String msg = String.format("Could not register user [%s]", user.getName());
        System.out.println(msg);
        return false;
    }

    public boolean saveById(int id) {
        User user = getById(id);
        return save(user);
    }

    private boolean save(User user) {
        boolean res = false;
        try {
            dataLock.writeLock().lock();
            res = save_unsafe(user);
        } finally {
            dataLock.writeLock().unlock();
        }
        return res;
    }

    private boolean save_unsafe(User user) {
        assert dataUrl != null;
        String filePath = String.format("%s%d", DataConstants.FILE_BASE, user.getId());
        File userFile = new File(filePath);
        if (!userFile.exists()) {
            try {
                if (userFile.createNewFile()) {
                    try (FileWriter writer = new FileWriter(userFile)) {
                        String data = getPersistenceData(user);
                        writer.write(data);
                    } catch (Exception e) {
                        System.err.println("Could not save user");
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    return true;
                }
            } catch (IOException e) {
                System.err.println("Could not save user");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        try (FileWriter writer = new FileWriter(userFile)) {
            String data = getPersistenceData(user);
            writer.write(data);
        } catch (Exception e) {
            System.err.println("Could not save user");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;

    }

    public String getPersistenceData(User user) {
        return String.format("%d,%s,%s,%d", user.getId(), user.getName(), user.getPassword(), user.getRank().getValue());
    }


    public void logIn(int id) {
        try {
            dataLock.readLock().lock();
            loggedInUsers.add(id);

        } finally {
            dataLock.readLock().unlock();
        }
    }

    public boolean isLoggedIn(int id) {
        boolean res = false;
        try {
            dataLock.readLock().lock();
            res = loggedInUsers.contains(id);
        } finally {
            dataLock.readLock().unlock();
        }

        return res;
    }

    public void logOut(int id) {
        try {
            dataLock.readLock().lock();
            loggedInUsers.remove(id);

        } finally {
            dataLock.readLock().unlock();
        }
    }

    private static class DataConstants {
        public static final int ID = 0;
        public static final int NAME = 1;
        public static final int PASSWORD = 2;
        public static final int RANK = 3;
        public static final String FILE_PATH = "user-data/";
        public static final String FILE_BASE = "cpd-server/src/main/resources/user-data/user-";
    }
}
