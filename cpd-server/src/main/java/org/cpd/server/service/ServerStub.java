package org.cpd.server.service;

import org.cpd.server.controller.AuthController;
import org.cpd.server.controller.GameController;
import org.cpd.shared.Config;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.RequestFactory;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerStub {

    private final UserRepository repository;

    public ServerStub(UserRepository repository){
        this.repository = repository;
    }

    public void initializeServer(Config config) throws IOException {
        final ExecutorService executor = Executors.newFixedThreadPool(config.maxThreads);
        ServerThread authServer = new ServerThread(config.authPort, new AuthController(repository));
        ServerThread gameServer = new ServerThread(config.serverPort, new GameController(repository, executor));
        executor.execute(authServer);
        String msg = String.format("Auth server started on port %d", config.authPort);
        System.out.println(msg);
        executor.execute(gameServer);
        msg = String.format("Game server started on port %d", config.serverPort);
        System.out.println(msg);
    }

    public static void sendMessage(SocketChannel socket, int userId, String msg) throws IOException, ClassNotFoundException {
        ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
        PlayRequest request = (PlayRequest) RequestFactory.newRequest(RequestType.PLAY, userId);
        request.setRequestBody(msg);
        os.writeObject(request);
    }
    public static void sendFeedBack(SocketChannel socket, int userId, String msg) throws IOException, ClassNotFoundException {
        ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
        FeedBackResponse request = (FeedBackResponse) ResponseFactory.newResponse(userId, Response.Status.OK,msg,ResponseType.FeedBack);
        os.writeObject(request);
    }

    public static PlayResponse sendTurn(SocketChannel socket, int userId){
        try{
            sendMessage(socket, userId, YOUR_TURN);
            ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
            return (PlayResponse) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(OH_BOY);
            e.printStackTrace();
        }
        return null;
    }

    public static void notifyWinner(SocketChannel socket, int userId){
        try{
            sendMessage(socket, userId, YOU_WIN);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(OH_BOY);
            e.printStackTrace();
        }
    }

    public static void notifyLoser(SocketChannel socket, int userId){
        try{
            sendMessage(socket, userId, YOU_LOSE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(OH_BOY);
            e.printStackTrace();
        }
    }

    public static final String YOUR_TURN = "It's your turn! Type in a message";
    public static final String YOU_WIN = "Congratulations! You win!";
    public static final String YOU_LOSE = "You've lost. So sad :(";
    public static final String OH_BOY = "Something went wrong :|";
    public static final String RECEIVED = "Your move was registered";

}
