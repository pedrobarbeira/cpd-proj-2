package org.cpd.server.controller;

import org.cpd.server.service.Game;
import org.cpd.server.service.MatchMaker;
import org.cpd.server.service.ServerStub;
import org.cpd.server.service.UserRepository;
import org.cpd.shared.Config;
import org.cpd.shared.User;
import org.cpd.shared.request.GameType;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.Request;
import org.cpd.shared.response.PlayResponse;
import org.cpd.shared.response.Response;
import org.cpd.shared.response.ResponseFactory;
import org.cpd.shared.response.ResponseType;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class GameController implements Controller {
    private final ExecutorService executor;
    private final UserRepository userRepository;

    public GameController(UserRepository userRepository, ExecutorService executor) {
        this.userRepository = userRepository;
        this.executor = executor;
    }

    @Override
    public Response handleRequest(Request request, SocketChannel socket) {
        System.out.println("Handling Request in game controller");
        PlayRequest playRequest = (PlayRequest) request;
        if(playRequest.isDisconnect()){
            return handleDisconnectRequest(playRequest);
        }else{
            GameType gameType = (GameType) playRequest.getRequestBody();
            Game game = null;
            switch(gameType){
                case RANK -> {}
                case NORMAL -> game = MatchMaker.findNormalMatch(request.getUserId(), socket);
            }
            if(game != null){
                executor.execute(game);
            }
        }
        return null;
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

    public static String sendTurn(SocketChannel socket, int userId) throws Exception {
        PlayResponse response = ServerStub.sendTurn(socket, userId);
        if(response != null) {
            if (response.getStatus() == Response.Status.OK) {
                return (String) response.getResponseBody();
            }
        }
        throw new Exception("Could not get response");
    }
    public static void sendMessage(SocketChannel socket, int userId,String message) throws  Exception{
        ServerStub.sendMessage(socket,userId,message);
    }

    public static void notifyWinner(SocketChannel socket, int userId) throws Exception {
        ServerStub.notifyWinner(socket, userId);
    }

    public static void notifyLoser(SocketChannel socket, int userId) throws Exception {
        ServerStub.notifyLoser(socket, userId);
    }

    private static class Messages{
        public static final String SAVE_ERROR = "There was an error saving user information";
        public static final String INVALID_TOKEN = "Bad request: invalid user token";
        public static final String DISCONNECTING = "Disconnecting user";
    }
}
