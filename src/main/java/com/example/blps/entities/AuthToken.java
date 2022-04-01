package com.example.blps.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity(name="auth_token")
@Table(name="auth_token")
public class AuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Size(min=4, max=20)
    @Column(unique = true)
    private String username;

    @NotEmpty
    @Size(min=4)
    @JsonIgnore
    @Column
    private String password;
}
