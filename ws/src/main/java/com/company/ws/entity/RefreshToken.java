package com.company.ws.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RefreshToken extends BaseEntity{
    private String refreshToken;
    private Date expireDate;
    @OneToOne
    private User user;
}
