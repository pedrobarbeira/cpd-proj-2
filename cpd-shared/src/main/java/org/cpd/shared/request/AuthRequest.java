package org.cpd.shared.request;

import org.cpd.shared.Config;
import org.cpd.shared.Credentials;

import java.time.LocalDateTime;
import java.util.List;

public class AuthRequest extends Request{
    public static final int NO_USER = 0;
    boolean isRegister = false;
    public AuthRequest(String requestId) {
        super(requestId, NO_USER, RequestType.AUTH);
    }

    public AuthRequest(String requestId, boolean isRegister) {
        super(requestId, NO_USER, RequestType.AUTH);
        this.isRegister = isRegister;
    }

    public AuthRequest(String requestId, LocalDateTime dateTime) {
        super(requestId, NO_USER, RequestType.AUTH, dateTime);
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
