package com.fiveguys.fivelogbackend.domain.blog.comment.service;



import com.fiveguys.fivelogbackend.domain.blog.board.entity.Board;
import com.fiveguys.fivelogbackend.domain.blog.comment.dto.CommentRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.comment.entity.Comment;
import com.fiveguys.fivelogbackend.domain.blog.comment.repository.CommentRepository;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final com.fiveguys.fivelogbackend.domain.blog.board.reposiroty.BoardRepository boardRepository;

    //댓글 쓰기

    public Comment save(Long id,CommentRequestDto request, String userName) {
        Optional<User> userOptional = userRepository.findByEmail(userName);
        User user;
        if (userOptional.isPresent()) { // Optional이 값으로 채워져 있는지 확인
            user = userOptional.get(); // User 객체 추출
        } else {
            System.out.println("사용자가 존재하지 않습니다: " + userName);
            return null;
        }
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

        request.setUser(user);
        request.setBoard(board);

        return commentRepository.save(request.toEntity());
    }


//    //댓글 수정
//    @Transactional
//    public String updateComment(Long id, String content){
//        return "hello";
//    }
//
//    //댓글 삭제
//    @Transactional
//    public void delete(Long id){
//        commentRepository.deleteById(id);
//    }


}
