package com.fiveguys.fivelogbackend.global.app;


import com.fiveguys.fivelogbackend.domain.blog.board.dto.CreateBoardRequestDto;
import com.fiveguys.fivelogbackend.domain.blog.board.entity.BoardStatus;
import com.fiveguys.fivelogbackend.domain.blog.board.service.BoardService;
import com.fiveguys.fivelogbackend.domain.user.user.controller.UserController;
import com.fiveguys.fivelogbackend.domain.user.user.dto.JoinUserDto;
import com.fiveguys.fivelogbackend.domain.user.user.entity.User;
import com.fiveguys.fivelogbackend.domain.user.user.repository.UserRepository;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserCommandService;
import com.fiveguys.fivelogbackend.domain.user.user.serivce.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final UserService userService;
    private final BoardService boardService;
    private final UserCommandService userCommandService;
    @Override
    public void run(String... args) throws Exception {
        if(userService.countUsers() == 0){
            for (int i = 1; i < 11; i++) {
                String email = "join@test.com" + i;
                String password = "1234";
                String nickname = "joinUser_" + i;
                userCommandService.joinAndCreateBlog ( email, password, nickname, "");
            }
            log.info ("✅ 테스트 사용자 10명 등록 완료");

            for (int i = 0; i < 15; i++) {
                for (int j = 1; j < 11; j++) {
                    CreateBoardRequestDto dto = CreateBoardRequestDto.builder()
                            .title("코딩 공부 Day" + (j + i + 1))
                            .status(BoardStatus.PUBLIC)
                            .content("안녕하세요! 오늘은 Java 웹 개발자라면 반드시 익혀야 할 Spring Boot에 대해 알아보려 합니다. 복잡한 설정 없이 빠르게 웹 애플리케이션을 만들 수 있어 많은 개발자들이 애용하는 프레임워크입니다.")
                            .hashtags(new String[]{"코딩", "공부"})
                            .build();
                    boardService.createBoard(dto, (long)j);
                }
            }
            log.info ("✅ 테스트 게시판 등록 완료");

        }


    }
}
