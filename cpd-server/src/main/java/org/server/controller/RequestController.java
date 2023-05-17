package org.server.controller;

import org.cpd.shared.response.ResponseFactory;

public abstract class RequestController {
    private final ResponseFactory responseFactory = new ResponseFactory();

    public ResponseFactory getResponseFactory(){
        return this.responseFactory;
    }
}
