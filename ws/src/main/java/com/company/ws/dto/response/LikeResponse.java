package com.company.ws.dto.response;

import com.company.ws.entity.Like;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LikeResponse {

    private long id;
    private long shareId;
    private UserResponse userResponse;


    public LikeResponse(Like like){
        this.id= like.getId();
        this.shareId=like.getShare().getId();
        this.userResponse=new UserResponse(like.getUser());
    }

}
