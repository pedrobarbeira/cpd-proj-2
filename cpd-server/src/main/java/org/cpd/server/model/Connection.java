package org.cpd.server.model;

import java.net.Socket;
import java.time.LocalDateTime;

public class Connection {
    LocalDateTime lastActive;
    private final Socket socket;
    private final String token;
    private Status status;

    public Connection(Socket socket, String token){
        this.socket = socket;
        this.token = token;
        this.status = Status.ACTIVE;
    }

    public boolean open(){
        if(this.status != Status.ACTIVE) {
            ping();
            this.status = Status.ACTIVE;
            return true;
        }
        return false;
    }

    public boolean close(){
        if(this.status != Status.DISCONNECTED){
            ping();
            this.status = Status.DISCONNECTED;
            return true;
        }
        return false;
    }

    public void ping(){
        this.lastActive = LocalDateTime.now();
    }

    private enum Status{
        DISCONNECTED,
        ACTIVE,
    }
}
