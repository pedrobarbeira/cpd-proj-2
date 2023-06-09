package org.cpd.server.controller;

import org.cpd.server.service.UserRepository;
import org.cpd.shared.Credentials;
import org.cpd.shared.User;
import org.cpd.shared.request.AuthRequest;
import org.cpd.shared.request.Request;
import org.cpd.shared.request.RequestType;
import org.cpd.shared.response.*;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class AuthController extends RequestController implements Controller {
    public static final int NULL_USER_ID = 0;
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String INVALID_REQUEST = "Invalid request type";
    public static final String USER_NOT_EXISTS = "User does not exist";
    public static final String USER_EXISTS = "User already exists";
    public static final String USER_LOGGED_IN = "User already logged in";

    private final UserRepository repository;

    public AuthController(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Response handleRequest(Request request, SocketChannel socket) {
        if (request.getType() == RequestType.AUTH) {
            AuthRequest authRequest = (AuthRequest) request;
            Credentials credentials = authRequest.getCredentials();
            User user;
            if (authRequest.isRegister()) {
                int id = repository.getNextId();
                user = new User(id, credentials.getName(), credentials.getPassword());
                if (!repository.addUser(user)) {
                    return ResponseFactory.newResponse(NULL_USER_ID, Response.Status.BAD, USER_EXISTS, ResponseType.AUTH);
                }
            } else {
                user = repository.getByName(credentials.getName());
                if (user == null) {
                    return ResponseFactory.newResponse(NULL_USER_ID, Response.Status.BAD, USER_NOT_EXISTS, ResponseType.AUTH);
                }
                if (!user.validate(credentials.getPassword())) {
                    return ResponseFactory.newResponse(NULL_USER_ID, Response.Status.BAD, INVALID_CREDENTIALS, ResponseType.AUTH);
                }
                if (repository.isLoggedIn(user.getId())) {
                    return ResponseFactory.newResponse(NULL_USER_ID, Response.Status.BAD, USER_LOGGED_IN, ResponseType.AUTH);
                }

            }
            String token = generateToken();
            user.setToken(token);
            repository.logIn(user.getId());
            return ResponseFactory.newResponse(user.getId(), Response.Status.OK, user, ResponseType.AUTH);
        }
        return ResponseFactory.newResponse(NULL_USER_ID, Response.Status.BAD, INVALID_REQUEST, ResponseType.AUTH);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
