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
import com.example.blps.util.XmlReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.xml.bind.JAXB;

import javax.security.auth.login.CredentialException;
import java.io.*;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final XmlReader xml;


    public boolean saveUser(RegisterDTO dto) {
        AuthToken token = new AuthToken();
        token.setUsername(dto.getUsername());
        token.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        token.setRole(Role.ROLE_USER);
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPatronimic(dto.getPatronimic());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        userRepository.save(user);
        AuthToken check = xml.getToken(dto.getUsername());
        if(check==null) {
            xml.writeUser(token);
        }
        else{
            return false;
        }
        return true;
    }


    public LoginResponse login(LoginDTO token) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(token.getUsername(), token.getPassword())
        );

        try {
            AuthToken check = xml.getToken(token.getUsername());
            if (bCryptPasswordEncoder.matches(token.getPassword(), check.getPassword())) {
                return new LoginResponse(token.getUsername(), jwtUtil.generateToken(token.getUsername()));
            }
            throw new CredentialException("");
        } catch (CredentialException e) {
            return null;
        }
    }


}
