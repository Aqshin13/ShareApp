package com.company.ws.repository;

import com.company.ws.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByActivationToken(String activationToken);

    Page<User> findAllByIdNot(long id, Pageable pageable);


    Optional<User> findByEmail(String email);


}
