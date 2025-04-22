package com.fiveguys.fivelogbackend.global.pagination;

public class PageUt {

    public static PageDto get10unitPageDto(int presentPage, int totalPage) {
        int startPage = presentPage + 1 - (presentPage % 10);
        int endPage = Math.min(startPage + 9, totalPage);

        return  PageDto.builder()
                    .startPage(startPage)
                    .endPage(endPage)
                    .currentPage(presentPage +1 )
                    .totalPage(totalPage)
                    .build();
    }
}
