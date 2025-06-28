package com.company.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Autowired
    TokenFilter tokenFilter;

//    @Autowired
//    CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests((authentication) ->
                authentication
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.GET,
                                        "/api/v1/users/test")).authenticated()
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.DELETE,
                                        "/api/v1/users/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.PUT,
                                        "/api/v1/users/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher("/api/v1/shares/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.POST,"/api/v1/likes/**")).authenticated()
                        .requestMatchers(AntPathRequestMatcher
                                .antMatcher(HttpMethod.DELETE,"/api/v1/likes/**")).authenticated()

                        .anyRequest().permitAll()
        );
        http.httpBasic(httpBasic -> httpBasic
                .authenticationEntryPoint(new AuthEntryPoint()));
//        http.exceptionHandling(x->x.accessDeniedHandler(customAccessDeniedHandler));
        http.csrf(csrf -> csrf.disable());
        http.headers(headers -> headers.disable());

        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
