package com.shivender.quiz.service;

import com.shivender.quiz.dao.QuestionDao;
import com.shivender.quiz.exceptions.ApiException;
import com.shivender.quiz.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public List<Question> getAllQuestions(){
        return questionDao.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        Boolean isCateGoryExist = questionDao.existsByCategory(category);
        if(!isCateGoryExist){
            throw new ApiException("Category "+category+" not exists.", HttpStatus.NOT_FOUND);
        }
        return questionDao.findByCategory(category);
    }

    public String addQuestion(Question question) {
        questionDao.save(question);
        return "Question added successfully.";
    }
}
