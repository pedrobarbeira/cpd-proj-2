package org.cpd.server.service;

import org.cpd.server.controller.AuthController;
import org.cpd.server.controller.GameController;
import org.cpd.server.model.ServerThread;
import org.cpd.shared.Config;
import org.cpd.shared.response.ResponseFactory;

import java.io.IOException;

public class ServerStub {

    private final UserRepository repository;

    public ServerStub(UserRepository repository){
        this.repository = repository;
    }

    public void initializeServer(Config config) throws IOException {
        ServerThread authServer = new ServerThread(config.authPort, new AuthController(repository));
        ServerThread gameServer = new ServerThread(config.serverPort, new GameController(repository));
        Thread authThread = new Thread(authServer);
        authThread.start();
        String msg = String.format("[Thread %d] Auth server started on port %d", authThread.getId(), config.authPort);
        System.out.println(msg);
        Thread gameThread = new Thread(gameServer);
        gameThread.start();
        msg = String.format("[Thread %d] Game server started on port %d", gameThread.getId(), config.serverPort);
        System.out.println(msg);
    }

}
