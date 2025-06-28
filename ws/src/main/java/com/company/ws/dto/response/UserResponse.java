package com.company.ws.dto.response;

import com.company.ws.entity.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {

    private long id;
    private String username;
    private String profileImageUrl;


    public UserResponse(User user) {
        this.id= user.getId();
        this.username = user.getUsername();
        this.profileImageUrl = user.getProfileImageUrl();

    }

}
