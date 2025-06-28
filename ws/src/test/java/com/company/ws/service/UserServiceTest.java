package com.company.ws.service;

import com.company.ws.dto.FileLocation;
import com.company.ws.dto.request.UserCreateRequest;
import com.company.ws.dto.response.UserResponse;
import com.company.ws.email.EmailService;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import com.company.ws.error.InvalidTokenException;
import com.company.ws.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private EmailService emailService;

    @Mock
    private FileService fileService;

    @InjectMocks
    private UserService userService;


    @Test
    public void givenUserCreateRequestWhenCreateUserThenSuccess() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("john");
        request.setPassword("12345");
        request.setEmail("john@example.com");

        when(encoder.encode("12345")).thenReturn("hashedPassword");

        userService.createUser(request);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1))
                .saveAndFlush(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("john", savedUser.getUsername());
        assertEquals("hashedPassword", savedUser.getPassword());
        assertEquals("john@example.com", savedUser.getEmail());
        assertNotNull(savedUser.getActivationToken());

        verify(encoder, times(1)).encode("12345");
        verify(emailService, times(1))
                .sendActivationToken(eq("john@example.com"), anyString());

    }

    @Captor
    ArgumentCaptor<List<String>> captor;

    @Test
    public void givenIdWhenDeleteUserThenSuccess() {
        User user = User.builder()
                .email("aa@mail.com")
                .username("john")
                .password("12345")
                .shares(List.of(Share.builder()
                        .imageUrl("test.jpeg")
                        .build()))
                .build();
        user.setId(1L);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(fileService, times(1))
                .deleteFile(user.getProfileImageUrl(), FileLocation.PROFILE);
        verify(fileService, times(1)).deleteAllFiles(captor.capture());
        verify(userRepository, times(1)).deleteById(user.getId());
        assertEquals("test.jpeg", captor.getValue().get(0));
        assertEquals(1, captor.getValue().size());
    }


    @Test
    public void givenUsernameWhenFindByUsernameThenReturnUser() {
        String username = "john";
        User user = User.builder()
                .username(username)
                .password("12345")
                .email("aa@mail.com")
                .build();
        given(userRepository.findByUsername(username))
                .willReturn(Optional.of(user));
        User user1 = userService.findByUsername(username);
        assertEquals("john", user1.getUsername());
        assertEquals(user, user1);
        verify(userRepository, times(1)).findByUsername(username);
    }


    @Captor
    ArgumentCaptor<Page<User>> captorUser;

    @Test
    public void givenIdNotNullWhenGetAllThenReturnGetAllUserWithLoggedInUser() {

//       given
        Long id = null;
        User user1 = User.builder()
                .username("user1")
                .profileImageUrl("user1")
                .build();
        User user2 = User.builder()
                .username("user2")
                .profileImageUrl("user2")
                .build();
        User user3 = User.builder()
                .username("user3")
                .profileImageUrl("user3")
                .build();

        user1.setId(1L);
        user2.setId(1L);
        user3.setId(1L);
        List<User> users = List.of(user1, user2, user3);
        Pageable pageable = PageRequest.of(0, 3);
        Page<User> page = new PageImpl<>(users, pageable, users.size());

//        when
        when(userRepository.findAll(pageable)).thenReturn(page);
        Page<UserResponse> userResponses = userService.getAll(null, pageable);

//        then
        verify(userRepository, times(1)).findAll(pageable);
        assertEquals(3, userResponses.getTotalElements());
    }


    @Test
    public void givenActivationTokenIsNullThenThrowInvalidTokenException() {
        String token = null;
        User user1 = User.builder()
                .username("user1")
                .profileImageUrl("user1")
                .build();
        user1.setId(1L);
        when(userRepository.findByActivationToken(token)).thenReturn(Optional.empty());
        assertThrows(InvalidTokenException.class,()->{
            userService.findByActivationToken(token);
        });
        verify(userRepository, times(1)).findByActivationToken(token);

    }


}