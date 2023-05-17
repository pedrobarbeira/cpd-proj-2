package org.cpd.server;

import org.cpd.server.service.ConnectionManager;
import org.cpd.server.service.UserRepository;
import org.cpd.shared.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final UserRepository userRepository = new UserRepository();
    private static final Config config = new Config();
    private static final ConnectionManager connectionManager = new ConnectionManager();

    public static void main(String[] args) {

        if (args.length < 1) return;

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();

                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String time = reader.readLine();

                System.out.println("New client connected: "+ time);
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}