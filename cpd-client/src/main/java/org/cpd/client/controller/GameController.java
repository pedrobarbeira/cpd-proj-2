package org.cpd.client.controller;

import org.cpd.client.service.ClientStub;
import org.cpd.shared.User;
import org.cpd.shared.request.GameType;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.RequestFactory;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.PlayResponse;
import org.cpd.shared.response.Response;

import java.io.IOException;

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
        return findMatch(GameType.NORMAL);
    }

    public String findRankedMatch(){
        return findMatch(GameType.RANK);
    }

    private PlayRequest makePlayRequest(Object body){
        PlayRequest request = (PlayRequest) RequestFactory.newRequest(RequestType.PLAY, user.getId());
        request.setToken(user.getToken());
        request.setRequestBody(body);
        return request;
    }

    private String findMatch(GameType gameType){
        PlayRequest request = makePlayRequest(gameType);
        try {
            PlayResponse response = (PlayResponse) stub.sendRequest(request);
            if(response != null){
                return (String) response.getResponseBody();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public String turn() {
        PlayResponse response = (PlayResponse) stub.turn();
        if(response != null) {
            return (String) response.getResponseBody();
        }
        return null;
    }

    public String makeMove(String move){
        PlayRequest request = makePlayRequest(move);
        try {
            PlayResponse response = (PlayResponse) stub.sendRequest(request);
            if(response != null){
                return (String) response.getResponseBody();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public boolean disconnect(User user){
        Response response = stub.disconnect(user);
        return response.getStatus() == Response.Status.OK;
    }


}
