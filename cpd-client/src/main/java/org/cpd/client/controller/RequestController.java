package org.cpd.client.controller;

import org.cpd.shared.Config;
import org.cpd.shared.User;
import org.cpd.shared.response.AuthResponse;
import org.cpd.shared.response.Response;
import org.cpd.client.service.ClientStub;
import org.cpd.shared.response.ResponseType;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class RequestController{
    private final ClientStub stub;
    public RequestController(ClientStub stub) {
        this.stub = stub;
    }

    public User authenticate(String username, String password){
        Response response = stub.authenticate(username, password);
        return parseAuthResponse(response);
    }

    public User register(String username, String password){
        Response response = stub.register(username, password);
        return parseAuthResponse(response);
    }

    private User parseAuthResponse(Response response){
        if(response.getResponseType() == ResponseType.AUTH){
            if(response.getStatus() == Response.Status.OK) {
                Config config = stub.getConfig();
                try {
                    SocketChannel socket = SocketChannel.open();
                    socket.connect(new InetSocketAddress(config.host, config.serverPort));
                    stub.registerSocket(socket);
                    return (User) response.getResponseBody();
                } catch (IOException e) {
                    System.out.print("Error: server is not running");
                    return null;
                }
            }
        }
        String error = (String) response.getResponseBody();
        System.out.println(error + "\n");
        return null;
    }

}
