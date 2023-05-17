package org.cpd.client.service;

import org.cpd.shared.Config;
import org.cpd.shared.request.AuthRequest;
import org.cpd.shared.request.RegisterRequest;
import org.cpd.shared.request.RequestFactory;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.Response;

import java.io.*;
import java.net.Socket;

public class ClientStub {
    private final Config config;
    private final RequestFactory requestFactory;

    public ClientStub(Config config){
        this.config = config;
        this.requestFactory = new RequestFactory(config);
    }

    private Response sendRequest(String msg, Socket socket) throws IOException {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(msg);
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String response = reader.readLine();
            return Response.deserialize(response);
    }

    public Response authenticate(String name, String password){
        AuthRequest request = (AuthRequest) requestFactory.newRequest(RequestType.AUTH);
        request.setCredentials(name, password);
        String msg = request.serialize();
        try {
            Socket socket = new Socket(config.host, config.authPort);
            return sendRequest(msg, socket);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public Response register(String name, String password){
        RegisterRequest request = (RegisterRequest) requestFactory.newRequest(RequestType.REGISTER);
        request.setCredentials(name, password);
        String msg = request.serialize();
        try {
            Socket socket = new Socket(config.host, config.authPort);
            return sendRequest(msg, socket);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void registerMatch(){}

    public void playGame(){}

    public void registerPlay(){}

}
