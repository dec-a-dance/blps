package com.example.blps.repositories;

import com.example.blps.entities.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    public Optional<AuthToken> findByUsername(String username);
}
