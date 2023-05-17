package org.cpd.shared.response;

import java.time.LocalDateTime;

public abstract class Response {

    protected static final String DELIMITER = ";";
    private final LocalDateTime date;
    private String responseId;
    private int userId;
    private int status;
    private final String type;
    private String responseBody;



    public Response(String responseId, int userId, int status, String responseBody, String type) {
        this(responseId, userId, status, responseBody, type, LocalDateTime.now());
    }

    public Response(String responseId, int userId, int status, String responseBody, String type, LocalDateTime dateTime) {
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

    public int getStatus(){
        return this.status;
    }

    public String getResponseBody(){
        return this.responseBody;
    }

    public void setResponseBody(String content){
        this.responseBody = content;
    }

    public String serialize(){
        String userString = String.valueOf(this.userId);
        String statusString = String.valueOf(this.status);
        String dateString = this.date.toString();
        return String.join(DELIMITER, responseId, userString, statusString, responseBody, dateString, type);
    }

    public static Response deserialize(String str){
        String[] data = str.split(DELIMITER);
        int userId = Integer.parseInt(data[DataIndex.USER_ID]);
        int status = Integer.parseInt(data[DataIndex.STATUS]);
        LocalDateTime date = LocalDateTime.parse(data[DataIndex.DATE]);
        Response response;
        switch(data[DataIndex.TYPE]){
            case ResponseType.AUTH
                -> response = new AuthResponse(data[DataIndex.RESPONSE_ID], userId, status, data[DataIndex.BODY], date);
            case ResponseType.PLAY
                    -> response = new PlayResponse(data[DataIndex.RESPONSE_ID], userId, status, data[DataIndex.BODY], date);
            default -> response = null;
        }
        return response;
    }

    private static class DataIndex {
        public static final int RESPONSE_ID = 0;
        public static final int USER_ID = 1;
        public static final int STATUS = 2;
        public static final int BODY = 3;
        public static final int DATE = 4;
        public static final int TYPE = 5;
    }

    public static class StatusCodes{
        public static final int OK = 200;
    }
}
