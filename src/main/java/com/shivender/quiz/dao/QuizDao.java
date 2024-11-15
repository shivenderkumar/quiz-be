package com.shivender.quiz.dao;
import com.shivender.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, String> {
}
