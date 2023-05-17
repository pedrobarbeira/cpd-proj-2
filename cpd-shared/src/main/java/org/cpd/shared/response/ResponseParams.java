package org.cpd.shared.response;

public class ResponseParams {
    private int userId;
    private int status;
    private String responseBody;

    public ResponseParams(int userId, int status, String responseBody){
        this.userId = userId;
        this.status = status;
        this.responseBody = responseBody;
    }

    public int getUserId(){
        return userId;
    }

    public int getStatus(){
        return status;
    }

    public String getResponseBody(){
        return responseBody;
    }
}
