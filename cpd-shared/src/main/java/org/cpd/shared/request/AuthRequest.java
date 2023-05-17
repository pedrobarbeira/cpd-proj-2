package org.cpd.shared.request;

import org.cpd.shared.Config;
import org.cpd.shared.Credentials;

import java.time.LocalDateTime;
import java.util.List;

public class AuthRequest extends Request{
    private static final String DELIMITER = ",";
    public AuthRequest(String requestId, Config config) {
        super(requestId, config.host, config.authPort, config.userId, RequestType.AUTH);
    }

    public AuthRequest(String requestId, String host, int port, int userId, LocalDateTime dateTime) {
        super(requestId, host, port, userId, RequestType.AUTH, dateTime);
    }

    public void setCredentials(String name, String password){
        String body = String.join(DELIMITER, List.of(name, password));
        super.setRequestBody(body);
    }

    public Credentials getCredentials(){
        String[] data = getRequestBody().split(DELIMITER);
        return new Credentials(data[DataIndex.NAME], data[DataIndex.PASSWORD]);
    }

    private static class DataIndex{
        public static int NAME = 0;
        public static int PASSWORD = 1;
    }

}
