package org.cpd.shared.request;

import org.cpd.shared.Config;

import java.time.LocalDateTime;
import java.util.List;

public class RegisterRequest extends Request{
    public RegisterRequest(String requestId, Config config) {
        super(requestId, config.host, config.authPort, config.userId, RequestType.REGISTER);
    }

    public RegisterRequest(String requestId, String host, int port, int userId, LocalDateTime dateTime) {
        super(requestId, host, port, userId, RequestType.REGISTER, dateTime);
    }

    public void setCredentials(String name, String password){
        String body = String.join(",", List.of(name, password));
        super.setRequestBody(body);
    }

}
