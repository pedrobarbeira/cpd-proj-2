package org.cpd.shared.request;

import org.cpd.shared.Config;

public class RequestFactory {
    private static int count = 0;


    public static Request newRequest(RequestType type, int userId){
        count++;
        String requestId = String.format(Constants.REQUEST_ID_FORMAT, count, userId);
        return switch (type) {
            case AUTH -> new AuthRequest(requestId);
            case REGISTER -> new AuthRequest(requestId, true);
            case PLAY -> new PlayRequest(requestId, userId);
            case DISCONNECT -> new PlayRequest(requestId, userId, true);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static class Constants{
        public static String REQUEST_ID_FORMAT = "r%d-u%d";
    }

}
