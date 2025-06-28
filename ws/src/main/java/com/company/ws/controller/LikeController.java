package com.company.ws.controller;

import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.response.LikeResponse;
import com.company.ws.dto.response.ResponseMessage;
import com.company.ws.entity.Like;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.repository.LikeRepository;
import com.company.ws.repository.ShareRepository;
import com.company.ws.repository.UserRepository;
import com.company.ws.service.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/share/{id}")
    public List<LikeResponse> getLikeForShare(@PathVariable long id) {
        return likeService.getLikesForShare(id)
                .stream()
                .map(LikeResponse::new)
                .collect(Collectors.toList());
    }


    @PostMapping("/share/{id}")
    public ResponseEntity<ResponseMessage> like(@AuthenticationPrincipal CurrentUser user,
                                                @PathVariable long id
    ) {
        likeService.likeShare(user,id);
        return ResponseEntity.ok(new ResponseMessage("The share is liked"));
    }


    @DeleteMapping("/share/{id}")
    @Transactional
    public ResponseMessage unlike(@AuthenticationPrincipal CurrentUser user,@PathVariable long id ){
       likeService.unlike(user,id);
        return new ResponseMessage("You disliked this share");
    }


}
