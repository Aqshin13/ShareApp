package com.company.ws.repository;

import com.company.ws.entity.Like;
import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like,Long> {


    void deleteByUserAndShare(User user, Share share);



    boolean existsByUserAndShare(User user,Share share);
}
