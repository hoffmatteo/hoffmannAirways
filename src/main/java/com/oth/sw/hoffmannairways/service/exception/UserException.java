package com.oth.sw.hoffmannairways.service.exception;

public class UserException extends Exception {
    private String username;

    public UserException(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


}
