package org.cpd.shared;

public class Credentials {

    private final String name;
    private final String password;

    public Credentials(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public boolean validate(String str){
        return this.password.equals(str);
    }
}
