package org.cpd.client;

import org.cpd.client.controller.GameController;
import org.cpd.client.controller.RequestController;
import org.cpd.shared.User;
import org.cpd.client.view.AuthView;
import org.cpd.client.view.PlayView;
import org.cpd.client.service.ClientStub;
import org.cpd.shared.Config;

/**
 * This program demonstrates a simple TCP/IP socket client.
 *
 * @author www.codejava.net
 */
public class Client {
    private static final Config config = new Config();
 
    public static void main(String[] args) {

        ClientStub stub = new ClientStub(config);
        RequestController requestController = new RequestController(stub);
        AuthView authMenu = new AuthView(requestController);
        User user = authMenu.run();
        if(user != null){
            GameController gameController = new GameController(requestController);
            PlayView playMenu = new PlayView(user, gameController);
        }

//        if (args.length < 2) return;
//
//        String hostname = args[0];
//        int port = Integer.parseInt(args[1]);
//
//        try (Socket socket = new Socket(hostname, port)) {
//
//            OutputStream output = socket.getOutputStream();
//            PrintWriter writer = new PrintWriter(output, true);
//            writer.println("new Date()?".toString());
//
//            InputStream input = socket.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//            String time = reader.readLine();
//
//            System.out.println(time);
//
//
//        } catch (UnknownHostException ex) {
//
//            System.out.println("Server not found: " + ex.getMessage());
//
//        } catch (IOException ex) {
//
//            System.out.println("I/O error: " + ex.getMessage());
//        }
    }
}