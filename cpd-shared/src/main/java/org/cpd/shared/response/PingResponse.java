package org.cpd.shared.response;

import java.time.LocalDateTime;

public class PingResponse extends Response{

    public PingResponse(String responseId, int userId, Response.Status status, Object responseBody, LocalDateTime dateTime) {
        super(responseId, userId, status, responseBody, ResponseType.PING, dateTime);
    }
}
