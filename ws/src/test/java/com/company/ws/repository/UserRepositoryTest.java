package com.company.ws.repository;

import com.company.ws.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {



    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                        .username("username")
                        .password("password")
                        .activationToken("activationToken")
                        .email("email")
                .build());
    }

    @Test
    public void givenUsernameWhenFindByUsernameThenReturnUser() {
        Optional<User> user = userRepository.findByUsername("username");
        assertTrue(user.isPresent());
        assertEquals("username", user.get().getUsername());

    }

    @Test
    void givenIdWhenFindByIdNotThenReturnUser() {
        // GIVEN
        User user1 = new User();
        user1.setUsername("user1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        userRepository.save(user2);
        // WHEN
        Pageable pageable = PageRequest.of(0, 4);
        Page<User> result = userRepository.findAllByIdNot(user1.getId(), pageable);
        // THEN
        assertEquals(2, result.getTotalElements());
        assertEquals("user2", result.getContent().get(1).getUsername());
        assertTrue(result.getContent().stream()
                .noneMatch(u -> u.getId().equals(user1.getId())));
    }

}