package org.cpd.shared.request;

import org.cpd.shared.Config;
import org.cpd.shared.Credentials;

import java.time.LocalDateTime;
import java.util.List;

public class AuthRequest extends Request{
    boolean isRegister = false;
    public AuthRequest(String requestId, Config config) {
        super(requestId, config.host, config.authPort, config.userId, RequestType.AUTH);
    }

    public AuthRequest(String requestId, Config config, boolean isRegister) {
        super(requestId, config.host, config.authPort, config.userId, RequestType.AUTH);
        this.isRegister = isRegister;
    }

    public AuthRequest(String requestId, String host, int port, int userId, LocalDateTime dateTime) {
        super(requestId, host, port, userId, RequestType.AUTH, dateTime);
    }

    public void setCredentials(String name, String password){
        super.setRequestBody(new Credentials(name, password));
    }

    public Credentials getCredentials(){
        return (Credentials) getRequestBody();
    }

    public boolean isRegister(){
        return isRegister;
    }

    private static class DataIndex{
        public static int NAME = 0;
        public static int PASSWORD = 1;
    }

}
