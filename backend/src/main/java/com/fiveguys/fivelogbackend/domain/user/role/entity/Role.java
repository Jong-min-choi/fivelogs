package com.fiveguys.fivelogbackend.domain.user.role.entity;

import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30, nullable = false)
    RoleType name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
