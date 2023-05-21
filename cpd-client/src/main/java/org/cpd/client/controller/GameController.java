package org.cpd.client.controller;

import org.cpd.client.service.ClientStub;
import org.cpd.shared.User;
import org.cpd.shared.response.Response;

public class GameController {
    private final ClientStub stub;
    private User user;

    public GameController(ClientStub stub) {
        this.stub = stub;
    }

    public void registerUser(User user){
        this.user = user;
    }

    public String findNormalMatch(){
        return null;
    }

    public String findRankedMatch(){
        return null;
    }

    public String turn(){
        return null;
    }

    public String makeMove(String move){
        return null;
    }


    public boolean disconnect(User user){
        Response response = stub.disconnect(user);
        return response.getStatus() == Response.Status.OK;
    }
}
