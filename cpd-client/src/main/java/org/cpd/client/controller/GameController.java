package org.cpd.client.controller;

import org.cpd.client.service.ClientStub;
import org.cpd.shared.User;
import org.cpd.shared.request.GameType;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.RequestFactory;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.PlayResponse;
import org.cpd.shared.response.Response;
import org.cpd.shared.response.ResponseFactory;
import org.cpd.shared.response.ResponseType;

import java.io.IOException;

public class GameController {
    private final ClientStub stub;
    private User user;

    public GameController(ClientStub stub) {
        this.stub = stub;
    }

    public void registerUser(User user) {
        this.user = user;
    }

    public String findNormalMatch() {
        return findMatch(GameType.NORMAL);
    }

    public String findRankedMatch() {
        return findMatch(GameType.RANK);
    }

    private PlayRequest makePlayRequest(Object body) {
        PlayRequest request = (PlayRequest) RequestFactory.newRequest(RequestType.PLAY, user.getId());
        request.setToken(user.getToken());
        request.setRequestBody(body);
        return request;
    }

    private PlayResponse makePlayResponse(Object body) {
        PlayResponse r  = (PlayResponse) ResponseFactory.newResponse(user.getId(), Response.Status.OK,body,ResponseType.PLAY);
        return r ;
    }

    private String findMatch(GameType gameType) {
        PlayRequest request = makePlayRequest(gameType);
        try {
            while (true) {
                PlayResponse response = (PlayResponse) stub.sendRequest(request);
                if (response != null) {
                    return (String) response.getResponseBody();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    public ServerMessage turn() {
        Message msg = stub.turn();
        if (msg == null) {
            return null;
        }
        if (msg.isRequest()) {
            var request = msg.getRequest();
            String message = (String) request.getRequestBody();
            return new ServerMessage(message, true, false);
        } else {
            var request = msg.getResponse();
            String message = (String) request.getResponseBody();
            return new ServerMessage(message, false, false);
        }
    }

    public void makeMove(String move) {
        PlayResponse response = makePlayResponse(move);
        try {
            stub.justSendRequest(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean disconnect(User user) {
        Response response = stub.disconnect(user);
        return response.getStatus() == Response.Status.OK;
    }


}
