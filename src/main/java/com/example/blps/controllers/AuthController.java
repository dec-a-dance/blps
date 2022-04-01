package com.example.blps.controllers;

import com.example.blps.dto.LoginDTO;
import com.example.blps.dto.RegisterDTO;
import com.example.blps.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto){
        boolean success = authService.saveUser(dto);
        if (success){
            return new ResponseEntity<>("User have been registered successfully.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Such user already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO dto){
        boolean success = authService.login(dto);
        if (success){
            return new ResponseEntity<>("Entered successfully.", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Wrong credentials.", HttpStatus.BAD_REQUEST);
        }
    }
}
