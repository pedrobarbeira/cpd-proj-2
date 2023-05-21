package org.cpd.server.model;

import org.cpd.server.controller.Controller;
import org.cpd.server.controller.GameController;
import org.cpd.server.service.Game;
import org.cpd.shared.Pair;
import org.cpd.shared.request.GameType;
import org.cpd.shared.request.PlayRequest;
import org.cpd.shared.request.Request;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.PlayResponse;
import org.cpd.shared.response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerThread implements Runnable{

    private final ServerSocketChannel serverSocket;

    private final Controller controller;

    public ServerThread(int port, Controller controller) throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        this.controller = controller;
    }

    @Override
    public void run() {
        while(true) {
            try (SocketChannel socket = serverSocket.accept()) {
                ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
                Request request = (Request) is.readObject();
                System.out.println("New client connected: " + request.getRequestBody());
                Response response = controller.handleRequest(request);
                switch(response.getResponseType()){
                    case DISCONNECT -> {
                        System.out.println("Disconnecting free socket");
                        socket.close();
                    }
                    case PLAY ->{
                        PlayRequest playRequest = (PlayRequest) request;
                        String token = playRequest.getToken();
                        GameType gameType = (GameType) playRequest.getRequestBody();
                        GameController gameController = (GameController) controller;
                        gameController.findMatch(token, socket, gameType);
                    }
                }
                ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
                os.writeObject(response);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
