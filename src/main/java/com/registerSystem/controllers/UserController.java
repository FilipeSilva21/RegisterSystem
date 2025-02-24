package com.registerSystem.controllers;

import com.registerSystem.DTOs.CreateUserDTO;
import com.registerSystem.models.User;
import com.registerSystem.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody CreateUserDTO createUserDTO){

        var userId = userService.createUser(createUserDTO);

        return ResponseEntity.created(URI.create("/v1/users/" + userId)).build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        var users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUser(@RequestParam String search){

        var users = userService.searchUser(search);

        return ResponseEntity.ok(users);
    }
}
