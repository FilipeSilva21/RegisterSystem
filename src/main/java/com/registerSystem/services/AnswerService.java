package com.registerSystem.services;

import com.registerSystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AnswerService {

    private final String BASE_URL = "http://localhost:8080/v1";

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> createUser(String user) {
        String url = BASE_URL + "/users";
        return restTemplate.postForEntity(url, user, String.class);
    }

    public ResponseEntity<String> getUsers() {
        String url = BASE_URL + "/users";
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<String> createQuestion(String question) {
        String url = BASE_URL + "/questions";
        return restTemplate.postForEntity(url, question, String.class);
    }

    public ResponseEntity<String> getQuestions() {
        String url = BASE_URL + "/questions";
        return restTemplate.getForEntity(url, String.class);
    }

    public ResponseEntity<Void> deleteQuestion(Long questionId) {
        String url = BASE_URL + "/questions/delete/" + questionId;
        restTemplate.delete(url);
        return ResponseEntity.ok().build();
    }

    public List<User> searchUser(String search) {
        String url = BASE_URL + "/users/search/" + search;

        User[] users = restTemplate.getForObject(url, User[].class);

        return users != null ? Arrays.asList(users) : List.of();
    }

}
