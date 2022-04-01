package com.example.blps.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name="category")
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
}
