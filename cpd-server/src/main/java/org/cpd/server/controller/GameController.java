package org.cpd.server.controller;

import org.cpd.server.service.UserRepository;
import org.cpd.shared.request.Request;
import org.cpd.shared.response.Response;

public class GameController implements Controller {
    private final UserRepository userRepository;


    public GameController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response handleRequest(Request request) {

        return null;
    }
}
