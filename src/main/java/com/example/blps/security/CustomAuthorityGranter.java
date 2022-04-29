package com.example.blps.security;

import com.example.blps.entities.AuthToken;
import com.example.blps.repositories.AuthTokenRepository;
import com.example.blps.services.AuthService;
import com.example.blps.util.XmlReader;
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
    private final XmlReader xml;

    @Override
    public Set<String> grant(Principal principal){
        String username = principal.getName();
        AuthToken token = xml.getToken(username);
        Role role = token.getRole();
        if (role==null){
            throw new RuntimeException("Failed to find role of this user.");
        };
        return Collections.singleton(role.name());
    }
}
