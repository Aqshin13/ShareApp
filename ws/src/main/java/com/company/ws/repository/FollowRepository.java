package com.company.ws.repository;

import com.company.ws.entity.Follow;
import com.company.ws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {



    List<Follow> getFollowByFollower(User user);

    List<Follow> getFollowByFollowingIdAndFollowStatus(long id, Follow.FollowStatus status);
    Optional<Follow> findByFollowingIdAndFollowerId(Long f_id,Long user_id);
    List<Follow> findFollowingByFollowerIdAndFollowStatus(long id, Follow.FollowStatus status);

}
