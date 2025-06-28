package com.company.ws.dto;

import java.io.File;

public enum FileLocation {


    PROFILE("./profile/"),
    SHARES("./shares/");

    final String location;


    FileLocation(String location) {
        this.location = location;
    }


    public String getValue(){
        return location;
    }


    }
