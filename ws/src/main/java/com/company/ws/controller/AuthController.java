package com.company.ws.controller;


import com.company.ws.dto.request.LoginRequest;
import com.company.ws.dto.response.AuthResponse;
import com.company.ws.entity.RefreshToken;
import com.company.ws.error.ExpireTokenException;
import com.company.ws.error.InvalidTokenException;
import com.company.ws.repository.RefreshTokenRepository;
import com.company.ws.service.AuthService;
import com.company.ws.service.RefreshTokenService;
import com.company.ws.service.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.auth(loginRequest);
    }

    @PostMapping("/refreshToken/{refreshToken}")
    public AuthResponse login(@PathVariable String refreshToken) {
        return authService.refreshToken(refreshToken);
    }


}
