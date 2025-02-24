package com.registerSystem.controllers;

import com.registerSystem.DTOs.CreateAnswerDTO;
import com.registerSystem.models.Answer;
import com.registerSystem.models.Question;
import com.registerSystem.services.AnswerService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<Void> createAnswer(@PathVariable("questionId") Long questionId, @RequestBody CreateAnswerDTO createAnswerDTO) {
        Long answerId = answerService.createAnswer(createAnswerDTO);

        return ResponseEntity.created(URI.create("/v1/answers/" + answerId)).build();
    }

    @GetMapping
    public ResponseEntity<List<Answer>> listAnswers() {

        return ResponseEntity.ok(answerService.getAllAnswers());
    }

    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        if (answerService.getAnswerById(answerId).isEmpty()) {
            throw new EntityNotFoundException("Pergunta não encontrada!");
        }

        answerService.deleteAnswer(answerId);

        return ResponseEntity.noContent().build();
    }
}

