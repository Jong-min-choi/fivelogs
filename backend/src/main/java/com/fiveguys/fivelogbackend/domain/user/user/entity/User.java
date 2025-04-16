package com.fiveguys.fivelogbackend.domain.user.user.entity;

import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.sort;
import static java.util.Arrays.stream;

@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    SNSLinks SNSLink;
    @Column(length = 255)
    String refreshToken;
    @Column(length = 20)
    String provider;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "image_id", nullable = true)
    Image profileImage;

    public boolean isAdmin() {
        return "admin".equals(email);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public User(long id, String email, String nickname) {
        this.setId(id);
        this.email = email;
        this.nickname = nickname;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(List<String> names) {
        return names
                .stream()
                .map((name) -> new SimpleGrantedAuthority("ROLE_" + name))
                .toList();
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin())
            authorities.add("ROLE_ADMIN");

        return authorities;
    }


}
