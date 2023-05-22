package org.cpd.client.controller;

import org.cpd.shared.request.Request;
import org.cpd.shared.response.Response;

import java.io.ObjectInputStream;

public class Message {
    private boolean isResponse;

    private Response response;
    private Request request;


    public Message(Response r) {
        isResponse = true;
        response = r;
        request = null;
    }

    public Message(Request r) {
        isResponse = false;
        response = null;
        request = r;
    }

    public boolean isResponse(){
        return  isResponse;
    }
    public boolean isRequest(){
        return !isResponse();
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
    public static Message fromStream(ObjectInputStream is){
        Object o ;
        try{
            o = is.readObject();
            if(o instanceof Response){
                return new Message((Response) o);
            }
            else if(o instanceof  Request){
                return  new Message((Request) o);
            }
            return null;
        } catch (Exception e){
            return null;
        }
    }
}
