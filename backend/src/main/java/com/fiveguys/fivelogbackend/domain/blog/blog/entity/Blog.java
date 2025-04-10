package com.fiveguys.fivelogbackend.domain.blog.blog.entity;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@NoArgsConstructor
@Getter
@ToString
@Table(name = "blogs")
public class Blog extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // 회원등록한 유저

    @OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boardList; // 블로그에 등록된 게시물들

}