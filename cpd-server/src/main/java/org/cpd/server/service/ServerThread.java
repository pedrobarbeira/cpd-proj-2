package org.cpd.server.service;

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
import org.cpd.shared.response.ResponseType;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerThread implements Runnable {

    private final ServerSocketChannel serverSocket;

    private final Controller controller;

    public ServerThread(int port, Controller controller) throws IOException {
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                SocketChannel socket = serverSocket.accept();
                ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
                Request request = (Request) is.readObject();
                System.out.println("New client connected: " + request.getRequestBody());
                Response response = controller.handleRequest(request, socket);
                if (response != null) {
                    ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
                    os.writeObject(response);
                    if (response.getResponseType().equals(ResponseType.DISCONNECT)) {
                        UserRepository.get().logOut(request.getUserId());
                        System.out.println("Disconnecting free socket");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
