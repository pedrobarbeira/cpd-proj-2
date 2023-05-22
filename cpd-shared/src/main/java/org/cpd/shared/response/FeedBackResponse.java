package org.cpd.shared.response;

public class FeedBackResponse extends  Response{
    public FeedBackResponse(String responseId, int userId, Status status, Object responseBody, ResponseType type) {
        super(responseId, userId, status, responseBody, type);
    }
}
