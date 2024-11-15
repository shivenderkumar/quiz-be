package com.shivender.quiz.service;

import com.shivender.quiz.dao.QuizDao;
import com.shivender.quiz.exceptions.ApiException;
import com.shivender.quiz.model.Question;
import com.shivender.quiz.model.QuestionWrapper;
import com.shivender.quiz.model.Quiz;
import com.shivender.quiz.model.QuizResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public Map<String, Object> submitQuiz(String id, Set<QuizResponse> quizResponseSet) {
        if(id == null || id.isEmpty()){
            throw new ApiException("Id required.", HttpStatus.BAD_REQUEST);
        }
        List<Question> questionList = quizDao.findById(id).orElseThrow(() -> {
            throw new ApiException("Quiz id does not exits.", HttpStatus.NOT_FOUND);
        }).getQuestionList();
        if (questionList == null || questionList.isEmpty()) {
            throw new ApiException("No questions found for the given quiz ID.", HttpStatus.NOT_FOUND);
        }

        Integer score = 0;
        for(QuizResponse qr : quizResponseSet){
            Question matchingQuestion = questionList.stream()
                    .filter(q -> q.getId().equals(qr.getId()))
                    .findFirst()
                    .orElse(null);
            if(matchingQuestion != null && matchingQuestion.getAnswer().equals(qr.getResponse())){
                score++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("quiz_id",id);
        result.put("total_questions", questionList.size());
        result.put("attempted_questions", quizResponseSet.size());
        result.put("score",score);

        return result;
    }
}
