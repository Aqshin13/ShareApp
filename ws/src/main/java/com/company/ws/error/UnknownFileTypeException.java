package com.company.ws.error;

public class UnknownFileTypeException extends RuntimeException {

    public UnknownFileTypeException(String message){
        super(message);
    }
}
