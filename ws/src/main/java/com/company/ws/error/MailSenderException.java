package com.company.ws.error;

public class MailSenderException extends RuntimeException{

    public MailSenderException(String message){
        super(message);
    }
}
