package org.cpd.shared.request;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Request implements Serializable {
    protected static final String DELIMITER = ";";

    private final LocalDateTime date;
    private final String requestId;
    private final int userId;
    private final String host;
    private final int port;
    private final RequestType requestType;
    private Object requestBody;


    public Request(String requestId, String host, int port, int userId, RequestType requestType){
        this.requestId = requestId;
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.requestType = requestType;
        this.date = LocalDateTime.now();
    }

    public Request(String requestId, String host, int port, int userId, RequestType requestType, LocalDateTime dateTime){
        this.requestId = requestId;
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.requestType = requestType;
        this.date = dateTime;
    }

    public void setRequestBody(Object requestBody){
        this.requestBody = requestBody;
    }

    public Object getRequestBody(){
        return this.requestBody;
    }

    public RequestType getType(){
        return this.requestType;
    }

    private static class DataIndex {
        public static final int REQUEST_ID = 0;
        public static final int HOST = 1;
        public static final int PORT = 2;
        public static final int USER_ID = 3;
        public static final int BODY = 4;
        public static final int TYPE = 5;
        public static final int DATE = 6;
    }
}
