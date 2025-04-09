package com.fiveguys.fivelogbackend.domain.post.post.user.entity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(length = 255, nullable = false)
    String password;
    @Column(length = 50, nullable = false)
    String email;
    @Column(length = 50, nullable = false)
    String nickname;
    @Column(length = 50, nullable = false)
    String introduce;
    @Column(name = "SNS_link")
    String SNSLink;
    @Column(length = 255)
    String refreshToken;

}
