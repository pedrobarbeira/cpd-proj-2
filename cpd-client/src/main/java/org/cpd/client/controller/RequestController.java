package org.cpd.client.controller;

import org.cpd.shared.User;
import org.cpd.shared.response.Response;
import org.cpd.client.service.ClientStub;

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
        if(response.getStatus() == Response.StatusCodes.OK){
            //TODO check if response is valid
            String responseBody = response.getResponseBody();
            return User.deserialize(responseBody);
        }
        //TODO maybe throw exception
        return null;
    }
}
