package org.cpd.server.controller;

import org.cpd.server.service.Game;
import org.cpd.server.service.UserRepository;
import org.cpd.shared.User;
import org.cpd.shared.request.GameType;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.Request;
import org.cpd.shared.response.Response;
import org.cpd.shared.response.ResponseFactory;
import org.cpd.shared.response.ResponseType;

import java.nio.channels.SocketChannel;

public class GameController implements Controller {
    private final UserRepository userRepository;

    public GameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {
        PlayRequest playRequest = (PlayRequest) request;
        if(playRequest.isDisconnect()){
            return handleDisconnectRequest(playRequest);
        }
        return null;
    }

    public void findMatch(String token, SocketChannel socket, GameType gameType){
        Game result;
        switch(gameType){
            case RANK -> {}
            case NORMAL -> {}
        }
    }

    private Response handleDisconnectRequest(PlayRequest request){
        int id = request.getUserId();
        String requestToken = request.getToken();
        String userToken = userRepository.getUserTokenById(id);
        if(requestToken.equals(userToken)) {
            if (!userRepository.saveById(id)) {
                return ResponseFactory.newResponse(id, Response.Status.BAD, Messages.SAVE_ERROR, ResponseType.DISCONNECT);
            }
            System.out.println(Messages.DISCONNECTING);
            //TODO add logic to remove socket from MM;
            return ResponseFactory.newResponse(id, Response.Status.OK, Messages.DISCONNECTING, ResponseType.DISCONNECT);
        }
        return ResponseFactory.newResponse(id, Response.Status.BAD, Messages.INVALID_TOKEN, ResponseType.DISCONNECT);
    }


    private static class Messages{
        public static final String SAVE_ERROR = "There was an error saving user information";
        public static final String INVALID_TOKEN = "Bad request: invalid user token";
        public static final String DISCONNECTING = "Disconnecting user";
    }
}
