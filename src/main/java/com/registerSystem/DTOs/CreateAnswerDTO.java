package com.registerSystem.DTOs;

import com.registerSystem.models.Question;

public record CreateAnswerDTO (String answer, Question questionId){
}
