package com.example.blps.repositories;

import com.example.blps.entities.AuthToken;
import com.example.blps.entities.User;
import com.example.blps.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByUsername(String username);

    default Optional<Role> findRoleByUsername(String username){
        Optional<AuthToken> authToken = findByUsername(username);
        return authToken.map(AuthToken::getRole);
    }
}
