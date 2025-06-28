package com.company.ws.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Share extends BaseEntity{

    private String imageUrl;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "share" ,cascade = CascadeType.REMOVE)
    private List<Comment> comments;
    @OneToMany(mappedBy = "share",cascade = CascadeType.REMOVE)
    private List<Like> likes;

}
