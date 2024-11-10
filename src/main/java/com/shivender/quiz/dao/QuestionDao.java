package com.shivender.quiz.dao;

import com.shivender.quiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDao extends JpaRepository<Question, String> {

    Boolean existsByCategory(String category);
    List<Question> findByCategory(String category);

}
