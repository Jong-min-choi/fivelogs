package com.fiveguys.fivelogbackend.domain.blog.comment.service;

import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.board.reposiroty.BoardRepository;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 작성
    public Comment firstComment(Comment comment, Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            comment.setBoard(board);
            return commentRepository.save(comment);
        }
        return null;
    }
    //댓글 수정
    public Comment editComment(Long boardId
            , Long id
            , Comment comment){
        return commentRepository.save(comment);
    }

    //댓글 삭제
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}

