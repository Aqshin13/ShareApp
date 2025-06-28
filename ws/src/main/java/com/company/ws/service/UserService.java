package com.company.ws.service;

import com.company.ws.dto.FileLocation;
import com.company.ws.dto.request.UserCreateRequest;
import com.company.ws.dto.response.UserResponse;
import com.company.ws.email.EmailService;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.*;
import com.company.ws.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FileService fileService;
    private final EmailService emailService;

    @Transactional(rollbackOn = MailException.class)
    public void createUser(UserCreateRequest userCreateRequest) {
        try {
            String activationToken = UUID.randomUUID().toString();
            userRepository.saveAndFlush(User.builder()
                    .username(userCreateRequest.getUsername())
                    .password(encoder.encode(userCreateRequest.getPassword()))
                    .activationToken(activationToken)
                    .email(userCreateRequest.getEmail())
                    .build());
            emailService.sendActivationToken(userCreateRequest.getEmail(), activationToken);
        } catch (MailException e) {
            throw new MailSenderException("Mail was not send");
        }


    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User is not found")
        );
    }


    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User is not found")
        );
    }


    public User findByActivationToken(String token) {
        Optional<User> user = userRepository.findByActivationToken(token);
        if (token==null || token.isEmpty() || user.isEmpty()) {
            throw new InvalidTokenException("Token is invalid");
        }
        return user.get();

    }


    public Page<UserResponse> getAll(Long id, Pageable pageable) {
        if (id == null) {
            return userRepository.findAll(pageable)
                    .map(UserResponse::new);
        }
        return userRepository.findAllByIdNot(id, pageable)
                .map(UserResponse::new);
    }


    public void activeUser(String token) {
        User user = findByActivationToken(token);
        user.setActive(true);
        user.setActivationToken(null);
        userRepository.save(user);
    }


    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User is not found")
        );
        List<String> shares = user
                .getShares()
                .stream()
                .map(Share::getImageUrl)
                .toList();
        fileService.deleteFile(user.getProfileImageUrl(),FileLocation.PROFILE);
        fileService.deleteAllFiles(shares);
        userRepository.deleteById(id);
    }


    public User update(long id, MultipartFile file) {
        if (file.isEmpty()) {
            throw new CommonException("File is empty", 400);
        }
        User user = findById(id);
        try {
            byte[] bytes=file.getBytes();
            if (user.getProfileImageUrl() != null) {
                fileService.deleteFile(user.getProfileImageUrl(), FileLocation.PROFILE);
            }
            fileService.detect(bytes);
            String fileName = fileService.writeFile(bytes, FileLocation.PROFILE);
            user.setProfileImageUrl(fileName);
        } catch (UnknownFileTypeException e) {
            throw new CommonException(e.getLocalizedMessage(), 400);
        } catch (Exception e) {
            throw new CommonException("Error is occurred while updating", 500);
        }
        return userRepository.save(user);

    }



    public User findByEmail(String email){
        return  userRepository.findByEmail(email).orElseThrow(
                ()->new UserNotFoundException("User is not found")
        );
    }


}
