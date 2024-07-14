package com.example.quiz_service.service;

import com.example.quiz_service.dao.QuizDao;
import com.example.quiz_service.feign.QuizInterface;
import com.example.quiz_service.model.QuestionWrapper;
import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {


    @Autowired
    QuizDao quizDao;

//    @Autowired
//    QuestionDao questionDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String categoryName, Integer numQuestions, String title) {


        // Generate http service method
        // RestTemplate

        List<Integer> questions = quizInterface.getQuestionsForQuiz(categoryName, numQuestions).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionList = quizInterface.getQuestionsFromId(questionIds);
        return questionList;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        System.out.println("calling  "+id);
      //  Quiz quiz = quizDao.findById(id).get();
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
