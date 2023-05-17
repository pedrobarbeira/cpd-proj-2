package org.cpd.shared.request;

import java.time.LocalDateTime;

public abstract class Request {
    protected static final String DELIMITER = ";";

    private final LocalDateTime date;
    private final String requestId;
    private final int userId;
    private final String host;
    private final int port;
    private final String requestType;
    private String requestBody;


    public Request(String requestId, String host, int port, int userId, String requestType){
        this.requestId = requestId;
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.requestType = requestType;
        this.date = LocalDateTime.now();
    }

    public Request(String requestId, String host, int port, int userId, String requestType, LocalDateTime dateTime){
        this.requestId = requestId;
        this.host = host;
        this.port = port;
        this.userId = userId;
        this.requestType = requestType;
        this.date = dateTime;
    }

    public void setRequestBody(String requestBody){
        this.requestBody = requestBody;
    }

    public String getRequestBody(){
        return this.requestBody;
    }

    public String serialize(){
        String portString = String.valueOf(this.port);
        String userString = String.valueOf(this.userId);
        String dateString = this.date.toString();
        return String.join(DELIMITER, requestId, host, portString, userString, requestBody, requestType, dateString);
    }

    public static Request deserialize(String str){
        String[] data = str.split(DELIMITER);
        int port = Integer.parseInt(data[DataIndex.PORT]);
        int id = Integer.parseInt(data[DataIndex.USER_ID]);
        String type = data[DataIndex.TYPE];
        Request request = null;
        LocalDateTime dateTime = LocalDateTime.parse(data[DataIndex.DATE]);
        switch (type) {
            case RequestType.AUTH
                    -> request = new AuthRequest(data[DataIndex.REQUEST_ID], data[DataIndex.HOST], port, id, dateTime);
            case RequestType.PLAY
                    -> request = new PlayRequest(data[DataIndex.REQUEST_ID], data[DataIndex.HOST], port, id, dateTime);
            case RequestType.REGISTER
                    -> request = new RegisterRequest(data[DataIndex.REQUEST_ID], data[DataIndex.HOST], port, id, dateTime);
        };
        if(request != null) {
            request.setRequestBody(data[DataIndex.BODY]);
        }
        //TODO maybe add exception here
        return request;
    }

    public String getType(){
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
