package com.example.blps.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity(name="user")
@Table(name="users")
public class User implements Serializable {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username", unique = true)
    @Size(min=4, max=20)
    private String username;

    private String name;

    private String surname;

    private String patronimic;

    private String email;

    private String phoneNumber;

    private String address;


}
