package org.cpd.shared.response;

import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.time.LocalDateTime;

public class AuthResponse extends Response{
    public AuthResponse(String responseId, int userId, Response.Status status, Object responseBody) {
        super(responseId, userId, status, responseBody, ResponseType.AUTH);
    }

    public AuthResponse(String responseId, int userId, Response.Status status, Object responseBody, LocalDateTime dateTime) {
        super(responseId, userId, status, responseBody, ResponseType.AUTH, dateTime);
    }

    public User getUser(){
        return (User) getResponseBody();
    }

    public void setContent(User user){
        String idString = String.valueOf(user.getId());
        Rank rank = user.getRank();
        String rankString = String.valueOf(rank.getValue());
        String contentString = String.join(idString, user.getName(), user.getPassword(), rankString, user.getToken());
        setResponseBody(contentString);
    }

    private static class DataIndex{
        public static final int ID = 0;
        public static final int NAME = 1;
        public static final int PASSWORD = 2;
        public static final int RANK = 3;
        public static final int TOKEN = 4;
    }
}
