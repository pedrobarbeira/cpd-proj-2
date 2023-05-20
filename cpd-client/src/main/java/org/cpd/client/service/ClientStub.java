package org.cpd.client.service;

import org.cpd.shared.request.*;
import org.cpd.shared.Config;
import org.cpd.shared.response.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;

public class ClientStub {
    private final Config config;
    private final RequestFactory requestFactory;

    public ClientStub(Config config){
        this.config = config;
        this.requestFactory = new RequestFactory(config);
    }

    private Response sendRequest(Request request, SocketChannel socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
        os.writeObject(request);
        ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
        return (Response) is.readObject();
    }

    private Response sendAuthRequest(Request request){
        try(SocketChannel socket = SocketChannel.open()){
            socket.connect(new InetSocketAddress(config.host, config.authPort));
            return sendRequest(request, socket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response authenticate(String name, String password){
        AuthRequest request = (AuthRequest) requestFactory.newRequest(RequestType.AUTH);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public Response register(String name, String password){
        AuthRequest request = (AuthRequest) requestFactory.newRequest(RequestType.REGISTER);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public void registerMatch(){}

    public void playGame(){}

    public void registerPlay(){}

}
