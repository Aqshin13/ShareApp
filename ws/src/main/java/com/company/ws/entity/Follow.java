package com.company.ws.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Follow extends BaseEntity{

    @ManyToOne
    private User following;
    @ManyToOne
    private User follower;
    @Enumerated(EnumType.STRING)
    private FollowStatus followStatus;


    public enum FollowStatus{
        ACCEPTED,
        PENDING
    }



}
