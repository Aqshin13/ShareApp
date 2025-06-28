package com.company.ws.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {


    private String message;
    private int statusCode;
    private long timeStamp=new Date().getTime();
    private Map<String,String> validationError;


}
