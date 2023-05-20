package org.cpd.shared.request;

import org.cpd.shared.Config;

public class RequestFactory {
    private final Config config;
    private int count;

    public RequestFactory(Config config){
        this.count = 0;
        this.config = config;
    }

    public Request newRequest(RequestType type){
        this.count++;
        String requestId = String.format(Constants.REQUEST_ID_FORMAT, this.count, this.config.userId);
        return switch (type) {
            case AUTH -> new AuthRequest(requestId, config);
            case PLAY -> new PlayRequest(requestId, config);
            case REGISTER -> new AuthRequest(requestId, config, true);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static class Constants{
        public static String REQUEST_ID_FORMAT = "r%d-u%d";
    }

}
