package com.company.ws.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment extends BaseEntity {

    private String text;
    @ManyToOne
    private User user;
    @ManyToOne
    private Share share;

}
