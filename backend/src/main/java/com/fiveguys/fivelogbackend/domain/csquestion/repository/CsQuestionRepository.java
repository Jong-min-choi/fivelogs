package com.fiveguys.fivelogbackend.domain.csquestion.repository;

import com.fiveguys.fivelogbackend.domain.csquestion.entitiy.CsQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CsQuestionRepository extends JpaRepository<CsQuestion, Long> {

    List<CsQuestion> findTop3ByOrderByCreatedDateDesc();
}
