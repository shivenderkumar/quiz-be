package com.shivender.quiz.service;

import com.shivender.quiz.dao.QuizDao;
import com.shivender.quiz.exceptions.ApiException;
import com.shivender.quiz.model.Question;
import com.shivender.quiz.model.QuestionWrapper;
import com.shivender.quiz.model.Quiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionService questionService;

    public String createQuiz(String category, String title, Integer numQ) {
        if(category == null || category.isEmpty() || title == null || title.isEmpty() || numQ == null || numQ<1){
            String errorMsg = "Please enter required fields numQ.";
            logger.error(errorMsg);
            throw new ApiException(errorMsg, HttpStatus.BAD_REQUEST);
        }
        List<Question> questionList = questionService.getNRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionList(questionList);

        quizDao.save(quiz);

        return "Quiz created successfully.";
    }

    public Quiz getQuiz(String id) {
        if(id == null || id.isEmpty()){
            throw new ApiException("Id required.", HttpStatus.BAD_REQUEST);
        }
        Optional<Quiz> quizOptional = quizDao.findById(id);
        return quizOptional.orElseThrow(() ->
                new ApiException("Quiz id does not exits.", HttpStatus.NOT_FOUND)
                );
    }

    public List<QuestionWrapper> getQuizQuestions(String id) {
        if(id == null || id.isEmpty()){
            throw new ApiException("Id required.", HttpStatus.BAD_REQUEST);
        }
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionList = quiz.orElseThrow(() -> {
            throw new ApiException("Quiz id does not exits.", HttpStatus.NOT_FOUND);
        }).getQuestionList();

        List<QuestionWrapper> questionWrapperList = new ArrayList<>();
        for(Question q : questionList){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionWrapperList.add(qw);
        }
        return questionWrapperList;
    }
}
