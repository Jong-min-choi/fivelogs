package com.fiveguys.fivelogbackend.domain.blog.board.repository;


import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}