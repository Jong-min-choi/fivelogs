package com.fiveguys.fivelogbackend.domain.blog.hashtag.service;

import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Hashtag;
import com.fiveguys.fivelogbackend.domain.blog.hashtag.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;

    @Transactional
    public List<Hashtag> createHashtags(List<String> hashTagNameList){

        List<String> normalized = hashTagNameList.stream()
                .map(String::toLowerCase)
                .distinct()
                .toList();

        List<Hashtag> existingHashtags = hashTagRepository.findAllByNameIn(normalized);

        //존재하는 hashTags
        Set<String> existingNames = existingHashtags.stream()
                .map(Hashtag::getName)
                .collect(Collectors.toSet());
        //존재하지 않는 hashTags
        List<Hashtag> hashtagsToSave = hashTagNameList.stream()
                .filter(name -> !existingNames.contains(name))
                .map(Hashtag::new)
                .collect(Collectors.toList());

        // 저장
        hashTagRepository.saveAll(hashtagsToSave);

        // 전체 해시태그 목록 (기존 + 신규)
        List<Hashtag> finalHashtags = new ArrayList<>();
        finalHashtags.addAll(existingHashtags);
        finalHashtags.addAll(hashtagsToSave);
        return finalHashtags;
    }

}
