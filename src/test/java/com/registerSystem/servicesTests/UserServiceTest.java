package com.registerSystem.servicesTests;

import com.registerSystem.DTOs.CreateUserDTO;
import com.registerSystem.models.User;
import com.registerSystem.repositories.UserRepository;
import com.registerSystem.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private CreateUserDTO validUserDTO;

    @BeforeEach
    void setUp() {
        validUserDTO = new CreateUserDTO( "João Silva", "joao@email.com", 18, 1.75);
        user = new User(1L, 25, "João Silva", "joao@email.com", 1.75);
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByEmail(validUserDTO.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        Long userId = userService.createUser(validUserDTO);

        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_WithShortName_ShouldThrowException() {
        CreateUserDTO shortNameUser = new CreateUserDTO( "Ana", "ana@email.com", 20, 1.60);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(shortNameUser));

        assertEquals("Nome do usuario deve ter no minimo 5 caracteres", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_WithInvalidEmail_ShouldThrowException() {
        CreateUserDTO invalidEmailUser = new CreateUserDTO("Maria Souza", "mariaemail.com", 25, 1.65);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(invalidEmailUser));

        assertEquals("Usuario de email não é valido pos deve conter '@'", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_Underage_ShouldThrowException() {
        CreateUserDTO underageUser = new CreateUserDTO("Pedro Santos", "pedro@email.com", 17, 1.70);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(underageUser));

        assertEquals("Usuario deve ter no minimo 18 anos", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_DuplicateEmail_ShouldThrowException() {
        when(userRepository.findByEmail(validUserDTO.email())).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.createUser(validUserDTO));

        assertEquals("Usuario com esse email ja cadastrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("João Silva", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("João Silva", foundUser.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(99L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void testSearchUser_ByName() {
        when(userRepository.findByNameContainingIgnoreCase("João")).thenReturn(List.of(user));

        List<User> users = userService.searchUser("João");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("João Silva", users.get(0).getName());
        verify(userRepository, times(1)).findByNameContainingIgnoreCase("João");
    }

    @Test
    void testSearchUser_ByEmail() {
        when(userRepository.findByNameContainingIgnoreCase("maria@email.com")).thenReturn(List.of()); // Simula que não encontrou pelo nome
        when(userRepository.findAllByEmail("maria@email.com")).thenReturn(List.of(user)); // Simula que encontrou pelo email

        List<User> users = userService.searchUser("maria@email.com");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findByNameContainingIgnoreCase("maria@email.com");
        verify(userRepository, times(1)).findAllByEmail("maria@email.com");
    }



    @Test
    void testGenerateFileAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        File file = userService.generateFileAllUsers();

        assertNotNull(file);
        assertTrue(file.exists());
        assertEquals("users.txt", file.getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGenerateFileUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        File file = userService.generateFileUserById(1L);

        assertNotNull(file);
        assertTrue(file.exists());
        String expectedFileName = "1 - JOÃO SILVA.TXT".toLowerCase();
        String actualFileName = file.getName().toLowerCase();
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGenerateFileUserById_NotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.generateFileUserById(99L));

        assertEquals("Usuário com o Id 99não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
    }
}
