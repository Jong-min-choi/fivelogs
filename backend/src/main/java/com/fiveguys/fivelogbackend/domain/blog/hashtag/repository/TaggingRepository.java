package com.fiveguys.fivelogbackend.domain.blog.hashtag.repository;

import com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardHashtagDto;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Tagging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaggingRepository extends JpaRepository<Tagging, Long> {
    @Query("SELECT tg.hashtag.name FROM Tagging tg WHERE tg.board.id = :boardId")
    List<String> findAllHashtagsByBoardId(Long boardId);

    @Query("SELECT new com.fiveguys.fivelogbackend.domain.blog.board.dto.BoardHashtagDto (t.board.id, t.hashtag.name) FROM Tagging t WHERE t.board.id IN :boardIds")
    List<BoardHashtagDto> findHashtagDtosByBoardIds(@Param("boardIds") List<Long> boardIds);

    @Query("SELECT new com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto(t.hashtag.name, COUNT(t.board.id)) " +
            "FROM Tagging t WHERE t.board.user.nickname = :nickname " +
            "GROUP BY t.hashtag.name")
    List<HashtagCountDto> findHashtagCountsByNickname(@Param("nickname") String nickname);

    @Query("SELECT new com.fiveguys.fivelogbackend.domain.blog.hashtag.dto.HashtagCountDto(t.hashtag.name, COUNT(t.board.id)) " +
            "FROM Tagging t " +
            "WHERE t.board.user.nickname = :nickname " +
            "AND t.board.status = com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus.PUBLIC " +  // Board의 status가 PUBLIC인 조건 추가
            "GROUP BY t.hashtag.name")
    List<HashtagCountDto> findHashtagCountsByNicknameAndPublicBoard(@Param("nickname") String nickname);


    @Modifying
    @Transactional
    @Query("DELETE FROM Tagging t WHERE t.board.id = :boardId")
    void deleteAllByBoardId(@Param("boardId") Long boardId);
}
