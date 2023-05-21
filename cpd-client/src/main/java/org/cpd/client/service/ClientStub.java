package org.cpd.client.service;

import org.cpd.shared.Config;
import org.cpd.shared.User;
import org.cpd.shared.request.*;
import org.cpd.shared.response.PlayResponse;
import org.cpd.shared.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ClientStub {
    public static final String SEND_ERROR = "Could not send request";
    private final Config config;
    private SocketChannel socket;

    public ClientStub(Config config){
        this.config = config;
    }

    public Config getConfig(){
        return this.config;
    }

    public void registerSocket(SocketChannel socket){
        this.socket = socket;
    }


    public Response authenticate(String name, String password){
        AuthRequest request = (AuthRequest) RequestFactory.newRequest(RequestType.AUTH, config.NO_USER);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public Response register(String name, String password){
        AuthRequest request = (AuthRequest) RequestFactory.newRequest(RequestType.REGISTER, config.NO_USER);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public Response disconnect(User user){
        PlayRequest request = (PlayRequest) RequestFactory.newRequest(RequestType.DISCONNECT, user.getId());
        request.setToken(user.getToken());
        try {
            return sendRequest(request);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(SEND_ERROR);
            return null;
        }
    }

    public void registerMatch(){}

    public void playGame(){}

    public void registerPlay(){}

    public Response turn(){
        try {
            ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
            return (Response) is.readObject();
        } catch (IOException | ClassNotFoundException e) {
            //TODO add reconnect protocol
            System.out.println("Something went wrong");
        }
        return null;
    }

    public Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        return sendRequest(request, socket);
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
            System.out.println(SEND_ERROR);
            e.printStackTrace();
            return null;
        }
    }
}
