package org.cpd.client.controller;

import org.cpd.shared.User;
import org.cpd.shared.response.AuthResponse;
import org.cpd.shared.response.Response;
import org.cpd.client.service.ClientStub;
import org.cpd.shared.response.ResponseType;

public class RequestController{
    private final ClientStub stub;
    public RequestController(ClientStub stub) {
        this.stub = stub;
    }

    public User authenticate(String username, String password){
        Response response = stub.authenticate(username, password);
        return parseAuthResponse(response);
    }

    public User register(String username, String password){
        Response response = stub.register(username, password);
        return parseAuthResponse(response);
    }

    private User parseAuthResponse(Response response){
        if(response.getResponseType() == ResponseType.AUTH){
            if(response.getStatus() == Response.Status.OK) {
                return (User) response.getResponseBody();
            }
        }
        //TODO maybe throw exception
        return null;
    }
}
