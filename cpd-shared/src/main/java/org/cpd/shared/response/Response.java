package org.cpd.shared.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Response implements Serializable {

    protected static final String DELIMITER = ";";
    private final LocalDateTime date;
    private String responseId;
    private int userId;
    private Status status;
    private final ResponseType type;
    private Object responseBody;



    public Response(String responseId, int userId, Status status, Object responseBody, ResponseType type) {
        this(responseId, userId, status, responseBody, type, LocalDateTime.now());
    }

    public Response(String responseId, int userId, Status status, Object responseBody, ResponseType type, LocalDateTime dateTime) {
        this.responseId = responseId;
        this.userId = userId;
        this.status = status;
        this.responseBody = responseBody;
        this.date = dateTime;
        this.type = type;
    }

    public String getResponseId(){
        return this.responseId;
    }

    public int getUserId(){
        return this.userId;
    }

    public Status getStatus(){
        return this.status;
    }

    public Object getResponseBody(){
        return this.responseBody;
    }

    public void setResponseBody(String content){
        this.responseBody = content;
    }

    public ResponseType getResponseType(){
        return this.type;
    }

    private static class DataIndex {
        public static final int RESPONSE_ID = 0;
        public static final int USER_ID = 1;
        public static final int STATUS = 2;
        public static final int BODY = 3;
        public static final int DATE = 4;
        public static final int TYPE = 5;
    }

    public enum Status{
        OK,
        BAD
    }
}
