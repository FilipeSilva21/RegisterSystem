package com.registerSystem.servicesTests;

import com.registerSystem.DTOs.CreateQuestionDTO;
import com.registerSystem.models.Question;
import com.registerSystem.repositories.QuestionRepository;
import com.registerSystem.services.QuestionService;
import jakarta.persistence.EntityNotFoundException;
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
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question question;
    private CreateQuestionDTO createQuestionDTO;

    @BeforeEach
    void setUp() {
        createQuestionDTO = new CreateQuestionDTO("O que é Java?");
        question = new Question(5L, "O que é Java?");
    }

    @Test
    void testCreateQuestion() {
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Long questionId = questionService.createQuestion(createQuestionDTO);

        assertNotNull(questionId);
        assertEquals(5L, questionId);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testCreateQuestion_WithShortText_ShouldThrowException() {
        CreateQuestionDTO shortQuestion = new CreateQuestionDTO("Oi");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                questionService.createQuestion(shortQuestion)
        );

        assertEquals("Pergunta deve ter no mínimo 4 caracteres", exception.getMessage());
        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void testGetAllQuestions() {
        when(questionRepository.findAll()).thenReturn(List.of(question));

        List<Question> questions = questionService.getAllQuestions();

        assertFalse(questions.isEmpty());
        assertEquals(1, questions.size());
        assertEquals("O que é Java?", questions.get(0).getText());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void testGetQuestionById_Found() {
        when(questionRepository.findById(5L)).thenReturn(Optional.of(question));

        Optional<Question> foundQuestion = questionService.getQuestionById(5L);

        assertTrue(foundQuestion.isPresent());
        assertEquals("O que é Java?", foundQuestion.get().getText());
        verify(questionRepository, times(1)).findById(5L);
    }

    @Test
    void testGetQuestionById_NotFound() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Question> foundQuestion = questionService.getQuestionById(99L);

        assertFalse(foundQuestion.isPresent());
        verify(questionRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteQuestion_Success() {
        when(questionRepository.findById(5L)).thenReturn(Optional.of(question));

        questionService.deleteQuestion(5L);

        verify(questionRepository, times(1)).deleteById(5L);
    }

    @Test
    void testDeleteQuestion_WithProtectedId_ShouldThrowException() {
        when(questionRepository.findById(4L)).thenReturn(Optional.of(new Question(4L, "Pergunta Protegida")));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                questionService.deleteQuestion(4L)
        );

        assertEquals("Só é permitido deletar questões a partir da 5.", exception.getMessage());
        verify(questionRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteQuestion_NotFound_ShouldThrowException() {
        when(questionRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                questionService.deleteQuestion(99L)
        );

        assertEquals("Questão não encontrada!", exception.getMessage());
        verify(questionRepository, never()).deleteById(anyLong());
    }
}
