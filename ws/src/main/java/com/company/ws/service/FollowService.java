package com.company.ws.service;


import com.company.ws.dto.CurrentUser;
import com.company.ws.entity.Follow;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.error.UserNotFoundException;
import com.company.ws.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;


    public Follow follow(long id, CurrentUser currentUser) {
        User follower = userService.findById(currentUser.getId());
        User following = userService.findById(id);
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow.setFollowStatus(Follow.FollowStatus.PENDING);
        return followRepository.save(follow);
    }


    public void unFollow(long id, long u_id) {
        Follow follow = followRepository
                .findByFollowingIdAndFollowerId(id, u_id)
                .orElseThrow(() ->
                        new CommonException("You are not follow already", 400));
        followRepository.delete(follow);
    }


    public void accept(long followId) {
        Follow follow = followRepository.findById(followId).orElseThrow(() ->
                new CommonException("resource is not found", 404));
        follow.setFollowStatus(Follow.FollowStatus.ACCEPTED);
        followRepository.save(follow);
    }

    public List<Follow> getFollowerStatus(long id) {
        User user = userService.findById(id);
        return followRepository.getFollowByFollower(user);
    }


    public List<Follow> getFollowByFollowing(long id) {
        return followRepository.getFollowByFollowingIdAndFollowStatus(id, Follow.FollowStatus.PENDING);
    }

    public void delete(Long id) {
        followRepository.deleteById(id);

    }
}
