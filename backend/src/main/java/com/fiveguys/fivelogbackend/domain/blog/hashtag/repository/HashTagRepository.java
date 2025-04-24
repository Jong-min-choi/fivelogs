package com.fiveguys.fivelogbackend.domain.blog.hashtag.repository;

import com.fiveguys.fivelogbackend.domain.blog.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);

    public boolean existsByName(String name);

    public List<Hashtag> findAllByNameIn(Collection<String> names);
}
