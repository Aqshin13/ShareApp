package com.company.ws.controller;

import com.company.ws.dto.request.UserCreateRequest;
import com.company.ws.dto.response.ResponseMessage;
import com.company.ws.dto.response.UserResponse;
import com.company.ws.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser(@Valid
                                                      @RequestBody
                                                      UserCreateRequest userCreateRequest) {
        userService.createUser(userCreateRequest);
        return ResponseEntity.ok(new ResponseMessage("Check your  mailbox"));
    }


    @GetMapping("/test")
    public String getTest() {
        return "Hello Agshin";
    }

    @GetMapping("/id/{id}")
    public UserResponse getUserById(@PathVariable long id) {
        return new UserResponse(userService.findById(id));

    }


    @GetMapping("/username/{username}")
    public UserResponse getUserByUsername(@PathVariable String username) {
        return new UserResponse(userService.findByUsername(username));

    }


    @GetMapping("/all")
    public Page<UserResponse> getAll(@RequestParam(required = false) Long id,
                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                     Pageable pageable) {
        return userService.getAll(id, pageable);

    }


    @PatchMapping("/activation/{token}")
    public ResponseMessage activeUser(@PathVariable String token) {
        userService.activeUser(token);
        return new ResponseMessage("User is activated");
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("#id==principal.id")
    public ResponseMessage deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseMessage("User is deleted");
    }


    @PutMapping("/{id}")
    @PreAuthorize("#id==principal.id")
    public UserResponse update(@PathVariable long id,
                               @RequestParam("file") MultipartFile request) {
        return new UserResponse(userService.update(id, request));
    }

}
