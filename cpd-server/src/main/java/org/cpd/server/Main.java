package org.cpd.server;

import org.cpd.server.service.MatchMaker;
import org.cpd.server.service.ServerStub;
import org.cpd.server.service.UserRepository;
import org.cpd.shared.Config;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        final UserRepository userRepository = new UserRepository();
        final Config config = new Config();
        MatchMaker.config(config);
        ServerStub stub = new ServerStub(userRepository);
        stub.initializeServer(config);
    }

}