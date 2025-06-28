package com.company.ws.service;

import com.company.ws.dto.CurrentUser;
import com.company.ws.entity.Like;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.repository.LikeRepository;
import com.company.ws.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ShareRepository shareRepository;
    private final UserService userService;
    private final LikeRepository likeRepository;

    public List<Like> getLikesForShare(long id) {
        Share share = shareRepository.findById(id).orElseThrow(() ->
                new CommonException("Share is not found", 404));
        return share.getLikes();
    }


    public void likeShare(CurrentUser user, long id) {
        Share share = shareRepository.findById(id).orElseThrow(() ->
                new CommonException("Share is not found", 404));
        User user1 = userService.findById(user.getId());
//        boolean alreadyLiked = share.getLikes().stream()
//                .anyMatch(like -> like.getUser().getId().equals(user1.getId()));
//        if (alreadyLiked) {
//            throw new CommonException("you liked this share already", 400);
//        }

        if (likeRepository.existsByUserAndShare(user1, share)) {
            throw new CommonException("you liked this share already", 400);
        }
        Like like = new Like();
        like.setShare(share);
        like.setUser(user1);
        likeRepository.save(like);

    }


    public void unlike(CurrentUser currentUser, long id) {
        Share share = shareRepository.findById(id).orElseThrow(() ->
                new CommonException("Share is not found", 404));
        User user1 = userService.findById(currentUser.getId());
//        boolean dontLiked = share.getLikes().stream()
//                .anyMatch(like -> like.getUser().getId().equals(user1.getId()));
//        if (!dontLiked) {
//            throw new CommonException("you  don't like this share already", 400);
//        }
        if (!likeRepository.existsByUserAndShare(user1, share)) {
            throw new CommonException("you  don't like this share already", 400);
        }
        likeRepository.deleteByUserAndShare(user1, share);
    }


}
