package org.cpd.shared.request;

import org.cpd.shared.Config;

import java.time.LocalDateTime;

public class PlayRequest extends Request{
    private String token;
    private boolean isDisconnect = false;

    public PlayRequest(String requestId, int userId) {
        super(requestId, userId, RequestType.PLAY);
    }

    public PlayRequest(String requestId, int userId, boolean isDisconnect) {
        super(requestId, userId, RequestType.PLAY);
        this.isDisconnect = isDisconnect;
    }

    public PlayRequest(String requestId, int userId, LocalDateTime dateTime) {
        super(requestId, userId, RequestType.PLAY, dateTime);
    }

    public void setMove(String move){
        super.setRequestBody(move);
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public boolean isDisconnect(){
        return this.isDisconnect;
    }
}
