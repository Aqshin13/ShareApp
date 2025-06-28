package com.company.ws.dto.response;


import com.company.ws.entity.Follow;
import lombok.Data;

@Data
public class FollowResponse {

    private Follow.FollowStatus followStatus;

    public FollowResponse(Follow.FollowStatus followStatus){
        this.followStatus=followStatus;
    }


}
