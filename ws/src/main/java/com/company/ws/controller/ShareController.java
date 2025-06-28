package com.company.ws.controller;

import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.response.ResponseMessage;
import com.company.ws.dto.response.ShareResponse;
import com.company.ws.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/shares")
@RequiredArgsConstructor
public class ShareController {


    private final ShareService shareService;

    @PostMapping("/create")
    public ResponseMessage createShare(@AuthenticationPrincipal CurrentUser currentUser,
                                       @RequestParam("file") MultipartFile imageData
    ) {
        shareService.createShare(imageData, currentUser);
        return new ResponseMessage("Share is created");
    }


    @DeleteMapping("/{id}")
    public ResponseMessage deleteShare(@PathVariable Long id,
                                       @AuthenticationPrincipal CurrentUser user) {
        shareService.deleteShare(id, user);
        return new ResponseMessage("Your share is deleted");

    }


    @GetMapping
    public Page<ShareResponse> getAll(@PageableDefault(sort = "id",
                                              direction = Sort.Direction.DESC)
                                      Pageable pageable,
                                      @AuthenticationPrincipal CurrentUser user) {
        return shareService.getAll(pageable, user)
                .map(ShareResponse::new);
    }


}
