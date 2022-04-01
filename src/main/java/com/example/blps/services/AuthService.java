package com.example.blps.services;

import com.example.blps.dto.LoginDTO;
import com.example.blps.dto.RegisterDTO;
import com.example.blps.entities.AuthToken;
import com.example.blps.entities.User;
import com.example.blps.repositories.AuthTokenRepository;
import com.example.blps.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Service
public class AuthService {
    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(AuthTokenRepository authTokenRepository,
                       UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.authTokenRepository = authTokenRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean saveUser(RegisterDTO dto){
        if(authTokenRepository.findByUsername(dto.getUsername())!=null){
            log.debug("User with username {} already exist.", dto.getUsername());
            return false;
        }
        AuthToken token = new AuthToken();
        token.setUsername(dto.getUsername());
        token.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
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

    public boolean login(LoginDTO token){
        AuthToken check = authTokenRepository.findByUsername(token.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(bCryptPasswordEncoder.encode(token.getPassword())==check.getPassword()){
            return true;
        }
        return false;
    }
}
