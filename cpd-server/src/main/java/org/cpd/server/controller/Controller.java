package org.cpd.server.controller;

import org.cpd.shared.request.Request;
import org.cpd.shared.response.Response;

public interface Controller {
    Response handleRequest(Request request);
}
