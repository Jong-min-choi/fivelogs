package com.fiveguys.fivelogbackend.domain.user.role.entity;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30, nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
