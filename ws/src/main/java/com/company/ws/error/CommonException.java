package com.company.ws.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException{

   private int status;

    public CommonException(String message,int status){
        super(message);
        this.status=status;
    }
}
