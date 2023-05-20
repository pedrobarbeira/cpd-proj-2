package org.cpd.shared.response;

public class ResponseFactory {
    private static int count;

    public ResponseFactory(){
        count = 0;
    }

    public static Response newResponse(int userId, Response.Status status, Object body, ResponseType type){
        count++;
        String requestId = String.format(Constants.REQUEST_ID_FORMAT, count, userId);
        return switch (type) {
            case AUTH -> new AuthResponse(requestId, userId, status, body);
            case PLAY -> new PlayResponse(requestId, userId, status, body);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static class Constants{
        public static String REQUEST_ID_FORMAT = "r%d-u%d";
    }

}
