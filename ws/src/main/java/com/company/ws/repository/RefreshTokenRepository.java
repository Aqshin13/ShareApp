package com.company.ws.repository;

import com.company.ws.entity.RefreshToken;
import com.company.ws.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByUser(User user);


    Optional<RefreshToken> findByRefreshToken(String refreshToken);


    void deleteByRefreshToken(String refreshToken);
}
