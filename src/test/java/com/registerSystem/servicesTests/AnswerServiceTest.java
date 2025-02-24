package com.registerSystem.servicesTests;

import com.registerSystem.DTOs.CreateAnswerDTO;
import com.registerSystem.models.Answer;
import com.registerSystem.models.Question;
import com.registerSystem.repositories.AnswerRepository;
import com.registerSystem.services.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerService answerService;

    private Question question;
    private Answer answer;
    private CreateAnswerDTO createAnswerDTO;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.setQuestionId(1L);
        question.setText("O que é Java?");

        createAnswerDTO = new CreateAnswerDTO("Java é uma linguagem de programação.", question);

        answer = new Answer(1L, question, "Java é uma linguagem de programação.");
    }

    @Test
    void testCreateAnswer() {
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        Long answerId = answerService.createAnswer(createAnswerDTO, question);

        assertNotNull(answerId);
        assertEquals(1L, answerId);
        verify(answerRepository, times(1)).save(any(Answer.class));
    }

    @Test
    void testGetAllAnswers() {
        when(answerRepository.findAll()).thenReturn(List.of(answer));

        List<Answer> answers = answerService.getAllAnswers();

        assertFalse(answers.isEmpty());
        assertEquals(1, answers.size());
        assertEquals("Java é uma linguagem de programação.", answers.get(0).getAnswer());
        verify(answerRepository, times(1)).findAll();
    }

    @Test
    void testGetAnswerById_Found() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        Optional<Answer> foundAnswer = answerService.getAnswerById(1L);

        assertTrue(foundAnswer.isPresent());
        assertEquals("Java é uma linguagem de programação.", foundAnswer.get().getAnswer());
        verify(answerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAnswerById_NotFound() {
        when(answerRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Answer> foundAnswer = answerService.getAnswerById(2L);

        assertFalse(foundAnswer.isPresent());
        verify(answerRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteAnswer() {
        when(answerRepository.findById(1L)).thenReturn(Optional.of(answer));

        answerService.deleteAnswer(1L);

        verify(answerRepository, times(1)).deleteById(1L);
    }
}
