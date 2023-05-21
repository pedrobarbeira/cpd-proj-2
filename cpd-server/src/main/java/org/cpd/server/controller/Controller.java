package org.cpd.server.controller;

import org.cpd.shared.request.Request;
import org.cpd.shared.response.Response;

import java.net.Socket;
import java.nio.channels.SocketChannel;

public interface Controller {
    Response handleRequest(Request request, SocketChannel socket);
}
