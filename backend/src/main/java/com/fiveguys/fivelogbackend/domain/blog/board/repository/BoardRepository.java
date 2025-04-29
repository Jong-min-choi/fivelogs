package com.fiveguys.fivelogbackend.domain.blog.board.repository;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndUserId(Long boardId, Long User);

    Page<Board> findByTitleContainingIgnoreCaseOrUser_NicknameContainingIgnoreCase(
            String title, String nickname, Pageable pageable
    );

//    Page<Board> findByHashtagsContainingIgnoreCase(String hashtag, Pageable pageable);


    @Query("""
    SELECT b FROM Board b
    JOIN FETCH b.user u
    LEFT JOIN FETCH u.profileImage
    JOIN FETCH b.blog bl
    WHERE b.id = :boardId
    """)
    Optional<Board> findWithUserAndProfileImageById(@Param("boardId") Long boardId);

    @Query("SELECT b FROM Board b JOIN FETCH b.user")
    Page<Board> findAllWithUser(Pageable pageable);

    @Query("SELECT b FROM Board b JOIN FETCH b.user where b.user.nickname = :nickname")
    Page<Board> findAllWithUserByNickname(@Param("nickname")String nickname,Pageable pageable);


    Optional<Board>  findFirstByIdLessThanAndUser_NicknameOrderByIdDesc(Long id, String nickname);

    Optional<Board> findFirstByIdGreaterThanAndUser_NicknameOrderByIdAsc(Long id, String nickname);

    Long countByUser_Nickname(String nickname);


    @Query("SELECT SUM(b.views) FROM Board b WHERE b.user.nickname = :nickname")
    Long countView(String nickname);

    @Query("""
        SELECT b FROM Board b
        WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        AND b.status = 'PUBLIC'
    """)
    Page<Board> searchByTitle(@Param("keyword") String keyword, Pageable pageable);

//    @Query("""
//    SELECT DISTINCT b FROM Board b
//    JOIN b.taggings t
//    JOIN t.hashtag h
//    WHERE (LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
//        OR LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
//    AND b.status = 'PUBLIC'
//""")
//    Page<Board> searchByTitleOrHashtag(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.user u " +
            "LEFT JOIN b.taggings t " +
            "LEFT JOIN t.hashtag h " +
            "WHERE b.status = 'PUBLIC' " +
            "AND (b.title LIKE %:title% " +
            "OR h.name IN :hashtags) " +
            "AND (:lastId = 0 OR b.id < :lastId) " +  // lastId가 0이면 조건 무시, 0이 아니면 ID가 큰 것만 조회
            "ORDER BY b.id DESC ")  // ID가 증가하는 순서대로 정렬
    Page<Board> searchByTitleOrHashtagPaging(
            @Param("title") String title,
            @Param("hashtags") Set<String> hashtags,
            @Param("lastId") Long lastId,  // 마지막 ID
            Pageable pageable);

}