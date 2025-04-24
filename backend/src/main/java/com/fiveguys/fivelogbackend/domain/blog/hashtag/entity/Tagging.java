package com.fiveguys.fivelogbackend.domain.blog.hashtag.entity;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "taggings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "hashtag_id"})
        }
)
@NoArgsConstructor
@Getter
public class Tagging {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public Tagging(Board board, Hashtag hashtag) {
        this.board = board;
        this.hashtag = hashtag;
    }
}
