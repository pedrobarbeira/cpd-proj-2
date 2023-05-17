package org.cpd.server.service;

import org.cpd.shared.User;

public class Auth {
    //TODO might be interesting to hash passwords
    public final UserRepository userRepository;

    public Auth(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User authUser(String name, String password){
        User user = userRepository.getByName(name);
        if(user.validate(password)) return user;
        else return null;
    }

    public boolean registerUser(String name, String password){
        int id = userRepository.getNextId();
        User user = new User(id, name, password);
        return userRepository.addUser(user);
    }
}
