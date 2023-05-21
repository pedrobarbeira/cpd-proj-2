package org.cpd.client.view;

import org.cpd.client.controller.RequestController;
import org.cpd.shared.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthView{
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final String invalidCharset = "[!@#$%^&*()+=\\[\\]{}:;'\",.<>?\\\\/]+";
    private final RequestController controller;

    public AuthView(RequestController controller){
        this.controller = controller;
    }

    public User run() {
        while (true) {
            System.out.println(Messages.LOGIN);
            System.out.println(Messages.REGISTER);
            System.out.println(Messages.EXIT);
            String option;
            try {
                while (true) {
                    System.out.print(Messages.PROMPT);
                    option = reader.readLine();
                    if (!(option.equals(Options.LOGIN)
                            || option.equals(Options.REGISTER)
                            || option.equals(Options.EXIT))) {
                        System.out.println(Messages.INVALID_OPTION);
                    } else break;
                }
                switch (option) {
                    case Options.LOGIN -> {
                        return auth(Options.LOGIN);
                    }
                    case Options.REGISTER -> {
                        return auth(Options.REGISTER);
                    }
                    case Options.EXIT -> {
                        return exit();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private User auth(String option){
        Pattern pattern = Pattern.compile(invalidCharset);
        Matcher matcher;
        String username = null;
        String password = null;
        if(option.equals(Options.LOGIN)){
            System.out.println(Messages.LOGIN_MENU);
        }else{
            System.out.println(Messages.REGISTER_MENU);
        }
        try {
            while(true) {
                System.out.print(Messages.USERNAME);
                username = reader.readLine();
                if (username.equals(Options.EXIT_STRING)) {
                    return null;
                }
                matcher = pattern.matcher(username);
                if (!matcher.find()) {
                    break;
                }
                System.out.println(Messages.INVALID_INPUT);
            }
            while(true){
                System.out.print(Messages.PASSWORD);
                password = reader.readLine();
                if (password.equals(Options.EXIT_STRING)) {
                    return null;
                }
                matcher = pattern.matcher(password);
                if(!matcher.find()){
                    break;
                }
                System.out.println(Messages.INVALID_INPUT);
            }
        }catch(IOException e){
            e.printStackTrace();;
        }
        if(option.equals(Options.LOGIN)){
            return controller.authenticate(username, password);
        }else{
            return controller.register(username, password);
        }
    }

    public static User exit(){
        System.out.println(Messages.GOODBYE);
        return null;
    }

    public static class Messages{
        public static final String LOGIN = String.format("\t[%s] Login", Options.LOGIN);
        public static final String REGISTER = String.format("\t[%s] Register", Options.REGISTER);
        public static final String EXIT = String.format("\n\t[%s] EXIT", Options.EXIT);
        public static final String GOODBYE = "\tGoodbye adventurer!\n";
        public static final String PROMPT = "$";
        public static final String LOGIN_MENU = String.format("#### Login Menu (%s to exit)", Options.EXIT_STRING);
        public static final String REGISTER_MENU = String.format("#### Register Menu (%s to exit)", Options.EXIT_STRING);
        public static final String USERNAME = "Username: ";
        public static final String PASSWORD = "Password: ";
        public static final String INVALID_INPUT = "Invalid Input: only letters, numbers - and _ are allowed";
        public static final String INVALID_OPTION = "Invalid Option: select one of the available options";
    }

    public static class Options{
        public static final String LOGIN = "1";
        public static final String REGISTER = "2";
        public static final String EXIT = "0";
        public static final String EXIT_STRING = ":q";
    }
}
