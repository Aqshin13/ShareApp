package com.company.ws.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "like_i")
public class Like extends BaseEntity{

 @ManyToOne
 private User user;
 @ManyToOne
 private Share share;



}
