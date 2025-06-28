package com.company.ws.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExpireTokenException extends RuntimeException{

    private int status;

    public ExpireTokenException(String message,int status){
        super(message);
        this.status=status;
    }
}
