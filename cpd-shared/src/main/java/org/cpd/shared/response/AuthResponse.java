package org.cpd.shared.response;

import org.cpd.shared.Rank;
import org.cpd.shared.User;

import java.time.LocalDateTime;

public class AuthResponse extends Response{
    public AuthResponse(String responseId, int userId, int status, String responseBody) {
        super(responseId, userId, status, responseBody, ResponseType.AUTH);
    }

    public AuthResponse(String responseId, int userId, int status, String responseBody, LocalDateTime dateTime) {
        super(responseId, userId, status, responseBody, ResponseType.AUTH, dateTime);
    }

    public User getContent(){
        String[] data = getResponseBody().split(DELIMITER);
        int id = Integer.parseInt(data[DataIndex.ID]);
        int rank = Integer.parseInt(data[DataIndex.RANK]);
        return new User(id, data[DataIndex.NAME], data[DataIndex.PASSWORD], rank, data[DataIndex.TOKEN]);
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
