package org.cpd.server.model;

import org.cpd.server.controller.Controller;
import org.cpd.shared.request.Request;
import org.cpd.shared.request.RequestType;
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
                ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
                os.writeObject(response);
                if(request.getType().equals(RequestType.DISCONNECT)){
                    System.out.println("Disconnecting from socket");
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
