package com.shivender.quiz.controller;
import com.shivender.quiz.model.Question;
import com.shivender.quiz.model.QuestionWrapper;
import com.shivender.quiz.model.Quiz;
import com.shivender.quiz.model.QuizResponse;
import com.shivender.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("quiz")
@RestControllerAdvice
public class QuizController {

    @Autowired
    QuizService quizService;

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable String id){
        return new ResponseEntity<>(quizService.getQuiz(id), HttpStatus.OK);
    }

    @GetMapping("questions/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable String id){
        return new ResponseEntity<>(quizService.getQuizQuestions(id), HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam String title, @RequestParam Integer numQ){
        return new ResponseEntity<>(quizService.createQuiz(category, title, numQ), HttpStatus.CREATED);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Map<String, Object>> submitQuiz(@PathVariable String id, @RequestBody Set<QuizResponse> quizResponseSet){
        return new ResponseEntity<>(quizService.submitQuiz(id, quizResponseSet), HttpStatus.OK);
    }
}
