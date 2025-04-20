package com.fiveguys.fivelogbackend.domain.blog.board.repository;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCase(
            String title, String nickname, Pageable pageable
    );

    Page<Board> findByHashtagsContainingIgnoreCase(String hashtag, Pageable pageable);


    @Query("""
    SELECT b FROM Board b
    JOIN FETCH b.user u
    JOIN FETCH b.blog bl
    WHERE b.id = :boardId
    """)
    Optional<Board> findWithUserById(@Param("boardId") Long boardId);

    @Query("SELECT b FROM Board b JOIN FETCH b.user")
    Page<Board> findAllWithUser(Pageable pageable);

    Optional<Board>  findFirstByIdLessThanAndUser_NicknameOrderByIdDesc(Long id, String nickname);

    Optional<Board> findFirstByIdGreaterThanAndUser_NicknameOrderByIdAsc(Long id, String nickname);

}