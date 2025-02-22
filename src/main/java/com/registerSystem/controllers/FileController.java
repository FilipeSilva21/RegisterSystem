package com.registerSystem.controllers;

import com.registerSystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("v1/files")
public class FileController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<File> generateFileAllUsers() {
        File file = userService.generateFileAllUsers();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .body(file);
    }

    @PostMapping("{userId}")
    public ResponseEntity<File> generateFileUserById(@PathVariable Long userId) {
        File file = userService.generateFileUserById(userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .body(file);
    }
}
