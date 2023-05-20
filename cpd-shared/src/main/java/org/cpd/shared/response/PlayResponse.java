package org.cpd.shared.response;

import java.time.LocalDateTime;

public class PlayResponse extends Response{

    public PlayResponse(String responseId, int userId, Response.Status status, Object responseBody) {
        super(responseId, userId, status, responseBody, ResponseType.PLAY);
    }

    public PlayResponse(String responseId, int userId, Response.Status status, Object responseBody, LocalDateTime dateTime) {
        super(responseId, userId, status, responseBody, ResponseType.PLAY, dateTime);
    }

    public void setWin(){
        this.setResponseBody(Result.WIN);
    }

    public void setLose(){
        this.setResponseBody(Result.LOSE);
    }

    public boolean won(){
        return getResponseBody().equals(Result.WIN);
    }

    public static class Result{
        public static final String WIN = "win";
        public static final String LOSE = "lose";
    }

}
