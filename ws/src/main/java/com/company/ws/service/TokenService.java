package com.company.ws.service;

import com.company.ws.entity.User;
import com.company.ws.error.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

public class TokenService {

    private final static SecretKey key = Keys.hmacShaKeyFor("secret-must-be-at-least-32-chars".getBytes());
    private final static ObjectMapper mapper = new ObjectMapper();

    public static String createToken(User user) {
        TokenSubject tokenSubject = new TokenSubject(user.getId(), user.isActive(), user.getUsername());
        try {
            String subject = mapper.writeValueAsString(tokenSubject);
            return Jwts.builder()
                    .setSubject(subject)
                    .setExpiration(new Date(new Date().getTime() + 900000000))
                    .signWith(key)
                    .compact();

        } catch (JsonProcessingException e) {
            throw new AuthException("Unauthorized");
        }
    }


    public static User verifyToken(String token) throws JsonProcessingException {
        if (token == null) return null;
        String mainToken = token.split(" ")[1];
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            Jws<Claims> claimsJwt = jwtParser.parseClaimsJws(mainToken);
            String subject = claimsJwt.getBody().getSubject();
            TokenSubject tokenSubject = mapper.readValue(subject, TokenSubject.class);
            User user = new User();
            user.setId(tokenSubject.id());
            user.setActive(tokenSubject.active());
            user.setUsername(tokenSubject.username());
            return user;
    }


    private static record TokenSubject(Long id, boolean active,String username) {

    }


}
