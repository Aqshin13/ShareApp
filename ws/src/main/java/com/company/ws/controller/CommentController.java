package com.company.ws.controller;

import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.request.CommentRequest;
import com.company.ws.dto.response.CommentResponse;
import com.company.ws.dto.response.ResponseMessage;
import com.company.ws.service.CommentService;
import com.company.ws.service.ShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ShareService shareService;

    @PostMapping("/shares")
    public ResponseMessage createComment(@AuthenticationPrincipal CurrentUser user,
                                         @RequestBody @Valid CommentRequest commentRequest
    ) {
        commentService.createComment(user, commentRequest);
        return new ResponseMessage("Comment is shared");
    }


    @DeleteMapping("/{id}")
    public ResponseMessage deleteComment(@PathVariable long id) {
        commentService.deleteComment(id);
        return new ResponseMessage("Comment is deleted");
    }


    @GetMapping("/shares/{shareId}")
    public List<CommentResponse> getAll(@PathVariable long shareId) {
        return shareService.getById(shareId)
                .getComments()
                .stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());


    }


}
