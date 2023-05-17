package org.server.model;

import org.server.controller.Controller;
import org.cpd.shared.request.Request;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable{
    private final ServerSocket serverSocket;
    private final Controller controller;

    public ServerThread(int port, Controller controller) throws IOException {
        serverSocket = new ServerSocket(port);
        this.controller = controller;
    }

    @Override
    public void run() {
        while(true) {
            try (Socket socket = serverSocket.accept()) {
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String data = reader.readLine();
                Request request = Request.deserialize(data);
                Response response = controller.handleRequest(request);
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(response.serialize());
                if(request.getType().equals(RequestType.DISCONNECT)){
                    System.out.println("Disconnecting from socket");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
