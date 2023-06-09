package org.cpd.client.service;

import org.cpd.client.controller.Message;
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

    public ClientStub(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return this.config;
    }

    public void registerSocket(SocketChannel socket) throws IOException {
        socket.configureBlocking(true);
        this.socket = socket;
    }


    public Response authenticate(String name, String password) {
        AuthRequest request = (AuthRequest) RequestFactory.newRequest(RequestType.AUTH, config.NO_USER);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public Response register(String name, String password) {
        AuthRequest request = (AuthRequest) RequestFactory.newRequest(RequestType.REGISTER, config.NO_USER);
        request.setCredentials(name, password);
        return sendAuthRequest(request);
    }

    public Response disconnect(User user) {
        PlayRequest request = (PlayRequest) RequestFactory.newRequest(RequestType.DISCONNECT, user.getId());
        request.setToken(user.getToken());
        try {
            return sendRequest(request);
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(SEND_ERROR);
            return null;
        }
    }

    public void registerMatch() {
    }

    public void playGame() {
    }

    public void registerPlay() {
    }

    public Message turn() {
        int sleepCounts = 0;
        while (true) {
            try {
                ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
                return Message.fromStream(is);
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                    sleepCounts = (sleepCounts + 1) % 5;
                    if (sleepCounts == 0) {
                        System.out.println("Still looking");
                    }
                } catch (InterruptedException ex) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Response sendRequest(Request request) throws IOException, ClassNotFoundException {
        return sendRequest(request, socket);
    }

    public void justSendRequest(Response r) throws IOException, ClassNotFoundException {
        justSendRequest(r, socket);
    }

    private void justSendRequest(Response r, SocketChannel socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
        os.writeObject(r);
    }

    private Response sendRequest(Request request, SocketChannel socket) throws IOException, ClassNotFoundException {
        ObjectOutputStream os = new ObjectOutputStream(socket.socket().getOutputStream());
        os.writeObject(request);
        ObjectInputStream is = new ObjectInputStream(socket.socket().getInputStream());
        return (Response) is.readObject();
    }

    private Response sendAuthRequest(Request request) {
        try (SocketChannel socket = SocketChannel.open()) {
            socket.connect(new InetSocketAddress(config.host, config.authPort));
            return sendRequest(request, socket);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(SEND_ERROR);
            e.printStackTrace();
            return null;
        }
    }
}
