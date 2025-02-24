package com.registerSystem.services;

import com.registerSystem.DTOs.CreateAnswerDTO;
import com.registerSystem.models.Answer;
import com.registerSystem.models.Question;
import com.registerSystem.repositories.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

   public Long createAnswer(CreateAnswerDTO createAnswerDTO, Question question){

       var answer = new Answer(
               null,
               question,
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
