package com.company.ws.service;

import com.company.ws.dto.request.LoginRequest;
import com.company.ws.dto.response.AuthResponse;
import com.company.ws.dto.response.UserResponse;
import com.company.ws.entity.RefreshToken;
import com.company.ws.entity.User;
import com.company.ws.error.AuthException;
import com.company.ws.error.ExpireTokenException;
import com.company.ws.error.InvalidTokenException;
import com.company.ws.error.UserNotFoundException;
import com.company.ws.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse auth(LoginRequest loginRequest) {
        User user = null;
        try {
            user = userService.findByUsername(loginRequest.getUsername());
        } catch (UserNotFoundException exception) {
            throw new AuthException("Invalid credential");
        }
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credential");
        }
        String token = TokenService.createToken(user);
        RefreshToken refreshTokenDB = refreshTokenService.findByUser(user);
        if (refreshTokenDB == null) {
            refreshTokenDB=  new RefreshToken();
            refreshTokenDB.setUser(user);
        }
        refreshTokenDB.setRefreshToken(UUID.randomUUID().toString());
        refreshTokenDB.setExpireDate(Date.from(Instant.now().plusSeconds(180)));
        refreshTokenService.save(refreshTokenDB);
        return AuthResponse.builder()
                .token(token)
                .userResponse(new UserResponse(user))
                .refreshToken(refreshTokenDB.getRefreshToken())
                .build();

    }


    public AuthResponse refreshToken(String refreshToken){
        RefreshToken refreshTokenDB= refreshTokenService
                 .findByRefreshToken(refreshToken)
                 .get();
        String token= TokenService.createToken(refreshTokenDB.getUser());
        return AuthResponse.builder()
                .token(token)
                .build();
    }


}
