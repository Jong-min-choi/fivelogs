package com.fiveguys.fivelogbackend.global.pagination;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PageDto {
    int startPage;
    int endPage;
    int currentPage;
    int totalPage;

}
