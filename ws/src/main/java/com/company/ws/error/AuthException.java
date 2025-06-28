package com.company.ws.error;

public class AuthException extends RuntimeException{
    public AuthException(String message){
        super(message);
    }
}
