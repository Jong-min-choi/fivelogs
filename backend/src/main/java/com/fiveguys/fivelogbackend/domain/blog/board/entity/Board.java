package com.fiveguys.fivelogbackend.domain.blog.board.entity;

@SuperBuilder
@MappedSuperClass
@NoArgsConstructor
@Entity
@Getter
@ToString
public class Board {
    private String title;
    private String content;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedBy
    private LocalDateTime updatedDate;
}
