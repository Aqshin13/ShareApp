package com.company.ws.dto.response;

import com.company.ws.entity.Share;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareResponse {

    private long id;
    private String imageUrl;
    private UserResponse userResponse;
    private List<LikeResponse> likeResponses;
    public ShareResponse(Share share) {
        this.id = share.getId();
        this.imageUrl = share.getImageUrl();
        this.userResponse = new UserResponse(share.getUser());
        this.likeResponses=share.getLikes().stream().map(LikeResponse::new).toList();
    }

}
