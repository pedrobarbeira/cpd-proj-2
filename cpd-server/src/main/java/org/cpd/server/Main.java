package org.cpd.server;

import org.cpd.server.service.ServerStub;
import org.cpd.server.service.UserRepository;
import org.cpd.shared.Config;
import org.cpd.shared.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        final UserRepository userRepository = new UserRepository();
        final Config config = new Config();
        ServerStub stub = new ServerStub(userRepository);
        stub.initializeServer(config);
    }

}