package org.cpd.shared.request;

import java.time.LocalDateTime;

public class PingRequest extends Request{

    public PingRequest(String requestId, String host, int port, int userId, LocalDateTime dateTime) {
        super(requestId, host, port, userId, RequestType.PING, dateTime);
    }

}
