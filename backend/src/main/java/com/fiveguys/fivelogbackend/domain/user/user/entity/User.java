package com.fiveguys.fivelogbackend.domain.user.user.entity;

import com.fiveguys.fivelogbackend.domain.user.follow.entity.Follow;
import com.fiveguys.fivelogbackend.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @Column(length = 50, nullable = false, unique = true)
    String email;
    @Column(length = 50, nullable = false, unique = true)
    String nickname;
    @Column(length = 50, nullable = false)
    String introduce;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "githubLink", column = @Column(name = "github_link")),
            @AttributeOverride(name = "instagramLink", column = @Column(name = "instagram_link")),
            @AttributeOverride(name = "twitterLink", column = @Column(name = "twitter_link"))
    })
    private SNSLinks snsLink;
    @Column(length = 255)
    String refreshToken;
    @Column(length = 20)
    String provider;
    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = true)
    Image profileImage;

    @OneToMany(mappedBy = "follow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> follower;

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followed;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus userStatus;

//    public boolean isAdmin() {
//        return "admin".equals(email);
//    }

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

//        if (isAdmin())
//            authorities.add("ROLE_ADMIN");

        return authorities;
    }
}
