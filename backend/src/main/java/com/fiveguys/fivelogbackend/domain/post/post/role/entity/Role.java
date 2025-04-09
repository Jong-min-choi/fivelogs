package com.fiveguys.fivelogbackend.domain.post.post.role.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30, nullable = false)
    String name;
}
