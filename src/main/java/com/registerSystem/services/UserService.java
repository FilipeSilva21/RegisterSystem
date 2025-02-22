package com.registerSystem.services;

import com.registerSystem.DTOs.CreateUserDTO;
import com.registerSystem.models.User;
import com.registerSystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Long createUser(CreateUserDTO createUserDTO){
        var user = new User(
                null,
                createUserDTO.age(),
                createUserDTO.name(),
                createUserDTO.email(),
                createUserDTO.height()
        );

        if(createUserDTO.name().length() < 10){
            throw new IllegalArgumentException("Nome do usuario deve ter no minimo 10 caracteres");
        }

        if(userRepository.findByEmail(createUserDTO.email()).isPresent()){
            throw new IllegalArgumentException("Usuario com esse email ja cadastrado");
        }

        if(createUserDTO.age() < 18){
            throw new IllegalArgumentException("Usuario deve ter no minimo 18 anos");
        }

        if (!createUserDTO.email().contains("@")){
            throw new IllegalArgumentException("Usuario de email não é valido pos deve conter '@'");
        }

        var userSaved = userRepository.save(user);

        return userSaved.getUserId();
    }

    public List<User> getAllUsers(){

        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId){

        return userRepository.findById(userId);
    }

    public List<User> searchUser(String search) {
        List<User> result = userRepository.findByNameContainingIgnoreCase(search);
        if (result.isEmpty()) {
            result = userRepository.findAllByEmail(search);
        }
        return result;
    }

    public File generateFileAllUsers() {
        File file = new File("users.txt");

        List<User> users = userRepository.findAll();

        StringBuilder userData = new StringBuilder();
        for (User user : users) {
            userData.append(" ").append(user.getUserId())
                    .append(" - Name: ").append(user.getName())
                    .append("\n");
        }

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(userData.toString());
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar o arquivo", e);
        }
        return file;
    }

    public File generateFileUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário com o Id " + userId + "não encontrado"));

        String fileName = userId + " - " + user.getName().toUpperCase() + ".txt";
        Path filePath = Path.of(fileName);
        try {
            String userData = "UserId: " + user.getUserId()
                    + "\nName: " + user.getName()
                    + "\nEmail: " + user.getEmail()
                    + "\nAltura: " + user.getHeight()
                    + "\nIdade: " + user.getAge();
            Files.write(filePath, userData.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar arquivo para o usuário do id: " + userId, e);
        }

        return filePath.toFile();
    }
}
