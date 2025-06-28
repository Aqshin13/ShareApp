package com.company.ws.dto.response;

import com.company.ws.entity.Follow;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FollowStatusResponse {


    private Long id;
    private UserResponse follower;
    private UserResponse following;
    private Follow.FollowStatus status;


    public  FollowStatusResponse(Follow follow){
        this.id=follow.getId();
        this.follower=new UserResponse(follow.getFollower());
        this.following=new UserResponse(follow.getFollowing());
        this.status=follow.getFollowStatus();

    }




}
