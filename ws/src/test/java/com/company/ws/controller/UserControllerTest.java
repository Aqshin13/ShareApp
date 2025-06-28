package com.company.ws.controller;

import com.company.ws.dto.request.UserCreateRequest;
import com.company.ws.error.UserNotFoundException;
import com.company.ws.security.SecurityConfig;
import com.company.ws.security.UserDetailsServiceImpl;
import com.company.ws.service.UserService;
import com.company.ws.utilities.UniqueEmailValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService  userService;


    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void givenUserCreateRequestWhenCreateUserThenReturnResponseSuccess() throws Exception {
       UserCreateRequest request= UserCreateRequest.builder()
                .email("aa11@mail.com")
                .username("tes11t")
                .password("P+4ssword")
                .build();
        when(userService.findByUsername(any())).thenThrow(new UserNotFoundException("Not found"));
        when(userService.findByEmail(any())).thenThrow(new UserNotFoundException("Not found"));

        ResultActions response = mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        is("Check your  mailbox")));

    }

}