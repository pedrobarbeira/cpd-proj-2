package org.cpd.shared.request;

import org.cpd.shared.Config;

import java.time.LocalDateTime;

public class PlayRequest extends Request{

    public PlayRequest(String requestId, Config config) {
        super(requestId, config.host, config.serverPort, config.userId, RequestType.PLAY);
    }

    public PlayRequest(String requestId, String host, int port, int userId, LocalDateTime dateTime) {
        super(requestId, host, port, userId, RequestType.PLAY, dateTime);
    }

    public void setMove(String move){
        super.setRequestBody(move);
    }
}
