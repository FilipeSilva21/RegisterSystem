package com.registerSystem.repositories;

import com.registerSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById (Long userId);

    Optional<User> findByEmail(String email);

    List<User> findAllByEmail(String email);

    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByAge(int age);

}
