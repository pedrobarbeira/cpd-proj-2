package org.cpd.shared.response;

public class ResponseFactory {
    private int count;

    public ResponseFactory(){
        this.count = 0;
    }

    public Response newResponse(int userId, int status, String body, String type){
        this.count++;
        String requestId = String.format(Constants.REQUEST_ID_FORMAT, this.count, userId);
        return switch (type) {
            case ResponseType.AUTH -> new AuthResponse(requestId, userId, status, body);
            case ResponseType.PLAY -> new PlayResponse(requestId, userId, status, body);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static class Constants{
        public static String REQUEST_ID_FORMAT = "r%d-u%d";
    }

}
