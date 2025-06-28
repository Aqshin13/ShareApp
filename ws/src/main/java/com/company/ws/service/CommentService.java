package com.company.ws.service;


import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.request.CommentRequest;
import com.company.ws.entity.Comment;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.error.UserNotFoundException;
import com.company.ws.repository.CommentRepository;
import com.company.ws.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ShareRepository shareRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    public void createComment(CurrentUser currentUser, CommentRequest commentRequest){
        Comment comment = new Comment();
        Share share = shareRepository.findById(commentRequest.getShareId())
                .orElseThrow(() ->
                        new CommonException("Share is not found", 404));
        User user1 = userService.findById(currentUser.getId());
        comment.setShare(share);
        comment.setUser(user1);
        comment.setText(commentRequest.getText());
        commentRepository.save(comment);
    }

    public void deleteComment(long id){
        shareRepository.deleteById(id);
    }

}
