package com.company.ws.dto.response;


import com.company.ws.entity.Comment;
import com.company.ws.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {

    private String text;
    private UserResponse userResponse;


    public CommentResponse(Comment comment) {
        this.text = comment.getText();
        this.userResponse = new UserResponse(comment.getUser());
    }


}
