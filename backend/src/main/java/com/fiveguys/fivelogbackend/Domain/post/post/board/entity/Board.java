package com.fiveguys.fivelogbackend.domain.post.post.board.entity;

@SuperBuilder
@MappedSuperClass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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