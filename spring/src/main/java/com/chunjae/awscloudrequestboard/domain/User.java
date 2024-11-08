package com.chunjae.awscloudrequestboard.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; //user, admin
}