package com.fiveguys.fivelogbackend.domain.blog.hashtag.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class HashtagCountDto {
    String name;
    Long count;
}
