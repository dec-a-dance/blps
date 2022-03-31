package com.example.blps.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name="category")
@Table(name="category")
public class Category {
    @Id
    private long id;

    private String name;
}
