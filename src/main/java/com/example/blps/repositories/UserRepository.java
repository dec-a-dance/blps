package com.example.blps.repositories;

import com.example.blps.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
