package com.company.ws.service;

import com.company.ws.dto.CurrentUser;
import com.company.ws.dto.FileLocation;
import com.company.ws.entity.Follow;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.CommonException;
import com.company.ws.error.UnknownFileTypeException;
import com.company.ws.repository.FollowRepository;
import com.company.ws.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final UserService userService;
    private final ShareRepository shareRepository;
    private final FollowRepository followRepository;
    private final FileService fileService;
    public void createShare(MultipartFile file, CurrentUser currentUser) {
        if (file.isEmpty()) {
            throw new CommonException("File is empty", 400);
        }
        User user = userService.findByUsername(currentUser.getUsername());

        try {
            byte[] bytesImage = file.getBytes();
            fileService.detect(bytesImage);
            String fileName = fileService.writeFile(bytesImage, FileLocation.SHARES);
            Share share = new Share();
            share.setUser(user);
            share.setImageUrl(fileName);
            shareRepository.save(share);
        } catch (UnknownFileTypeException e) {
            throw new CommonException(e.getMessage(), 400);
        } catch (Exception e) {
            throw new CommonException("File is not uploaded", 500);
        }
    }


    public void deleteShare(long id, CurrentUser currentUser) {
        Share share = shareRepository.findById(id).orElseThrow(
                () -> new CommonException("Share is not found", 404)
        );
        if (share.getUser().getId() != currentUser.getId()) {
            throw new CommonException("Forbidden", 403);
        }
        shareRepository.deleteById(id);
    }


    public Page<Share> getAll(Pageable pageable, CurrentUser currentUser) {
        List<Long> allowedUserIds = followRepository
                .findFollowingByFollowerIdAndFollowStatus(currentUser.getId(),
                        Follow.FollowStatus.ACCEPTED)
                .stream()
                .map(f -> f.getFollowing().getId())
                .collect(Collectors.toList());
        allowedUserIds.add(currentUser.getId());
        System.out.println(allowedUserIds);

//        User user = userService.findById(currentUser.getId());
        return shareRepository.findShares(allowedUserIds,pageable);
    }

    public Share getById(long id) {
        return shareRepository.findById(id).orElseThrow(
                () -> new CommonException("Share isn't found", 404)
        );
    }

}
