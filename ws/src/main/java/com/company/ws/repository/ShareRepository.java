package com.company.ws.repository;

import com.company.ws.entity.Share;
import com.company.ws.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @Query("""
                SELECT s FROM Share s
                WHERE s.user = :user
                   OR s.user.id IN (
                       SELECT f.follower.id FROM Follow f
                       WHERE f.following = :user AND f.followStatus = 'ACCEPTED'
                   )
            """)
    Page<Share> findVisibleSharesForUser(@Param("user") User user, Pageable pageable);

    @Query("""
    SELECT s FROM Share s
    WHERE s.user.id IN :allowedUserIds
""")
    Page<Share> findShares(@Param("allowedUserIds") List<Long> allowedUserIds,
                           Pageable pageable);
}
