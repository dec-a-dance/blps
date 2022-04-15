package com.example.blps.services;

import com.example.blps.dto.LoginDTO;
import com.example.blps.dto.LoginResponse;
import com.example.blps.dto.RegisterDTO;
import com.example.blps.entities.AuthToken;
import com.example.blps.entities.User;
import com.example.blps.repositories.AuthTokenRepository;
import com.example.blps.repositories.UserRepository;
import com.example.blps.security.JwtUtil;
import com.example.blps.security.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.security.auth.login.CredentialException;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public boolean saveUser(RegisterDTO dto){
        if(authTokenRepository.findByUsername(dto.getUsername()).isPresent()){
            log.debug("User with username {} already exist.", dto.getUsername());
            return false;
        }
        AuthToken token = new AuthToken();
        token.setUsername(dto.getUsername());
        token.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        token.setRole(Role.ROLE_USER);
        authTokenRepository.save(token);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronimic(dto.getPatronimic());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        userRepository.save(user);
        return true;
    }

    public AuthToken findByUsername(String username){
        return authTokenRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public LoginResponse login(LoginDTO token){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(token.getUsername(), token.getPassword())
        );

        try {
            AuthToken check = authTokenRepository.findByUsername(token.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (bCryptPasswordEncoder.matches(token.getPassword(), check.getPassword())) {
                return new LoginResponse(token.getUsername(), jwtUtil.generateToken(token.getUsername()));
            }
            throw new CredentialException("");
        }
        catch(CredentialException e){
            return null;
        }
    }
}
