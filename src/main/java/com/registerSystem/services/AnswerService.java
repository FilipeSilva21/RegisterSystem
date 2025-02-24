package com.registerSystem.services;

import com.registerSystem.DTOs.CreateAnswerDTO;
import com.registerSystem.models.Answer;
import com.registerSystem.models.Question;
import com.registerSystem.models.User;
import com.registerSystem.repositories.AnswerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    private final String BASE_URL = "http://localhost:8080/v1";

    @Autowired
    private AnswerRepository answerRepository;

   public Long createAnswer(CreateAnswerDTO createAnswerDTO){
       var answer = new Answer(
               null,
               createAnswerDTO.questionId(),
               createAnswerDTO.answer()
       );

       var answerSaved = answerRepository.save(answer);

       return answerSaved.getAnswerId();
   }

   public List<Answer> getAllAnswers() {

       return answerRepository.findAll();
   }

    public Optional<Answer> getAnswerById (Long answerId){

        return answerRepository.findById(answerId);
    }

    public void deleteAnswer(Long answerId) {

        Optional<Answer> answer = answerRepository.findById(answerId);

        answerRepository.deleteById(answerId);
    }

}
