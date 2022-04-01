package com.example.blps.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterDTO {
    private String username;

    private String password;

    private String name;

    private String surname;

    private String patronimic;

    private String email;

    private String phoneNumber;

    private String address;
}
