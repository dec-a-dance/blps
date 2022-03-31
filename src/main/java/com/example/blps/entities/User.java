package com.example.blps.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name="user")
@Table(name="users")
public class User {
    @Id
    private long id;

    private String username;

    private String name;

    private String surname;

    private String patronimic;

    private String email;

    private String phoneNumber;

    private String address;


}
