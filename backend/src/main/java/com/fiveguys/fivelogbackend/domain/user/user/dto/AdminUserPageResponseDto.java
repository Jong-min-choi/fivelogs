package com.fiveguys.fivelogbackend.domain.user.user.dto;

import com.fiveguys.fivelogbackend.global.pagination.PageDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class AdminUserPageResponseDto {

    List<AdminUserResponseDto> adminUserResponseDto;
    PageDto pageDto;

}
