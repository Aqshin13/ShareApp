package com.company.ws.service;

import com.company.ws.entity.RefreshToken;
import com.company.ws.entity.User;
import com.company.ws.error.ExpireTokenException;
import com.company.ws.error.InvalidTokenException;
import com.company.ws.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository  refreshTokenRepository;

    public void save(RefreshToken refreshToken){
        refreshTokenRepository.save(refreshToken);
    }


    public static boolean isRefreshExpired(RefreshToken token) {
        return token.getExpireDate().before(new Date());
    }


    public Optional<RefreshToken> findByRefreshToken(String refreshToken){
        Optional<RefreshToken> refreshTokenDB=
                refreshTokenRepository.findByRefreshToken(refreshToken);
        if(refreshTokenDB.isEmpty()){
            throw new InvalidTokenException("Invalid refresh token");
        }
        if(RefreshTokenService.isRefreshExpired(refreshTokenDB.get())){
            throw new ExpireTokenException("Refresh Token is expired",498);
        }

        return refreshTokenDB;
    }



    public  RefreshToken findByUser(User user){
        return refreshTokenRepository.findByUser(user)
                .orElse(null);
    }
}
