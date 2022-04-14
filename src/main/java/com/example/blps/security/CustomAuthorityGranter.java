package com.example.blps.security;

import com.example.blps.repositories.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.jaas.AuthorityGranter;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorityGranter implements AuthorityGranter {
    private final AuthTokenRepository authTokenRepository;

    @Override
    public Set<String> grant(Principal principal){
        String username = principal.getName();
        Optional<Role> optional = authTokenRepository.findRoleByUsername(username);
        Role role = optional.orElseThrow(() -> new RuntimeException("Failed to find role of this user."));
        return Collections.singleton(role.name());
    }
}
