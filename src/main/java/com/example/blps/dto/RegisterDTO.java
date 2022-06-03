package com.example.blps.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String name;

    private String surname;

    private String patronimic;
    @NotNull
    private String email;

    private String phoneNumber;

    private String address;
}
