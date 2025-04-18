package com.fiveguys.fivelogbackend.global.pagination;

public class PageUt {

    public static PageDto get10unitPageDto(int presentPage, int totalPage) {
        int startPage = presentPage - (presentPage % 10);
        int endPage = Math.min(startPage + 9, totalPage);

        boolean isFirst = startPage == 1;
        boolean isLast = endPage == totalPage;
        return  PageDto.builder()
                    .startPage(startPage)
                    .endPage(endPage)
                    .currentPage(presentPage )
                    .totalPage(totalPage)
                    .isFirst(isFirst)
                    .isLast(isLast)
                    .build();
    }
}
