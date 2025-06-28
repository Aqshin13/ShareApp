package com.company.ws.controller;


import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.response.FollowResponse;
import com.company.ws.dto.response.FollowStatusResponse;
import com.company.ws.dto.response.ResponseMessage;
import com.company.ws.entity.Follow;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.error.UserNotFoundException;
import com.company.ws.repository.FollowRepository;
import com.company.ws.repository.UserRepository;
import com.company.ws.service.FollowService;
import com.company.ws.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{id}")
    public FollowResponse follow(@PathVariable Long id,
                                 @AuthenticationPrincipal CurrentUser user) {
        return new FollowResponse(followService.follow(id, user).getFollowStatus());

    }


    @DeleteMapping("/unfollow/{userId}")
    public ResponseMessage unfollow(@PathVariable Long userId,
                                    @AuthenticationPrincipal CurrentUser user) {
        followService.unFollow(userId, user.getId());
        return new ResponseMessage("User is unfollowed");

    }

    @PostMapping("/accept/{id}")
    public FollowResponse accept(@PathVariable Long id) {
        followService.accept(id);
        return new FollowResponse(Follow.FollowStatus.ACCEPTED);

    }

    @DeleteMapping("/reject/{id}")
    public ResponseMessage reject(@PathVariable Long id) {
        followService.delete(id);
        return new ResponseMessage("Follow is rejected");

    }


    @GetMapping("/follow-status/{id}")
    public List<FollowStatusResponse> getAllFollowById(@PathVariable long id) {
        List<Follow> follow = followService.getFollowerStatus(id);
        return follow
                .stream()
                .map(FollowStatusResponse::new)
                .collect(Collectors.toList());

    }


    @GetMapping("/follow-request/{id}")
    public List<FollowStatusResponse> getRequest(@PathVariable long id) {
        return followService.getFollowByFollowing(id)
                .stream()
                .map(FollowStatusResponse::new)
                .toList();
    }

}
