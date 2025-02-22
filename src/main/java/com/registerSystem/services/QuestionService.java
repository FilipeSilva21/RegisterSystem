package com.registerSystem.services;

import com.registerSystem.DTOs.CreateQuestionDTO;
import com.registerSystem.models.Question;
import com.registerSystem.repositories.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Long createQuestion(CreateQuestionDTO createQuestionDTO) {

        var question = new Question(
                null,
                createQuestionDTO.text()
        );

        if (createQuestionDTO.text().length() < 4) {
            throw new IllegalArgumentException("Pergunta deve ter no mínimo 4 caracteres");
        }

        var questionSaved = questionRepository.save(question);

        return questionSaved.getQuestionId();
    }

    public List<Question> getAllQuestions() {

        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById (Long questionId){

        return questionRepository.findById(questionId);
    }

    public void deleteQuestion(Long questionId) {

        Optional<Question> question = questionRepository.findById(questionId);

        if (question.isEmpty()) {
            throw new EntityNotFoundException("Questão não encontrada!");
        }  if (questionId <= 4) {
            throw new IllegalArgumentException("Só é permitido deletar questões a partir da 5.");
        }

        questionRepository.deleteById(questionId);
    }
}




