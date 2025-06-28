package com.company.ws.security;

import com.company.ws.dto.CurrentUser;
import com.company.ws.entity.User;
import com.company.ws.error.ExpireTokenException;
import com.company.ws.error.InvalidTokenException;
import com.company.ws.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {


    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        User user = null;
        if (token != null) {
            try {
                user = TokenService.verifyToken(token);
            } catch (SignatureException | JsonProcessingException | MalformedJwtException e) {
                exceptionResolver.resolveException(request, response, null, new InvalidTokenException("Token is invalid"));
                return;
            }catch (ExpiredJwtException e){
                exceptionResolver.resolveException(request, response, null, new ExpireTokenException("Token is expired",499));
                return;
            }
            if (user != null) {
                if (!user.isActive()) {
                    exceptionResolver.resolveException(request, response, null, new DisabledException("User is disabled"));
                    return;
                }
                CurrentUser userDetails = (CurrentUser) userDetailsService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }


}
