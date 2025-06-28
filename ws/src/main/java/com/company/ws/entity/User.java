package com.company.ws.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name="users",uniqueConstraints =  @UniqueConstraint(columnNames = {"email","username"}))
public class User extends BaseEntity {

    private String username;
    private String password;
    private String profileImageUrl;
    private boolean active=false;
    private String email;
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Share> shares;
    @OneToMany(mappedBy = "following",cascade = CascadeType.REMOVE)
    private List<Follow> following;
    @OneToMany(mappedBy = "follower",cascade = CascadeType.REMOVE)
    private List<Follow> follower;
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private List<Like>  likes;
    @OneToOne(mappedBy = "user",cascade = CascadeType.REMOVE)
    private RefreshToken refreshToken;
    private String activationToken;


}
