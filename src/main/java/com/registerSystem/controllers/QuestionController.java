package com.registerSystem.controllers;

import com.registerSystem.DTOs.CreateQuestionDTO;
import com.registerSystem.models.Question;
import com.registerSystem.services.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/questions")
    public ResponseEntity<Void> createQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        Long questionId = questionService.createQuestion(createQuestionDTO);

        return ResponseEntity.created(URI.create("/v1/questions/" + questionId)).build();
    }

    @GetMapping("/questions")
    public ResponseEntity<List<Question>> listQuestions() {

        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<Void> deleteQuestions(@PathVariable Long questionId) {
        if (questionService.getQuestionById(questionId).isEmpty()) {
            throw new EntityNotFoundException("Pergunta não encontrada!");
        }

        questionService.deleteQuestion(questionId);

        return ResponseEntity.noContent().build();
    }
}
