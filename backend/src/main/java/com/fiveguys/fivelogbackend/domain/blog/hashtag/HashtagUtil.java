package com.fiveguys.fivelogbackend.domain.blog.hashtag;

import java.util.Arrays;
import java.util.stream.Collectors;

public class HashtagUtil {

    public static String cleanHashtag(String rawInput) {
        return Arrays.stream(rawInput.split("\\s+")) //   \\s	 공백, 탭, 줄바꿈 등 모든 공백 문자 +는 1개이상을 뜻함 공백이 여러개여도 1개로 취급!
                .map(tag -> tag.startsWith("#") ? tag : "#" + tag)
                .distinct()
                .collect(Collectors.joining(" "));
    }
    public static String joinHashtags(String[] hashtags) {
        return Arrays.stream(hashtags)
                .filter(tag -> tag != null && !tag.isBlank())
                .map(tag -> tag.endsWith(",") ? tag : tag + ",")
                .distinct()
                .collect(Collectors.joining(","));
    }



}
