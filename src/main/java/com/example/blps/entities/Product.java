package com.example.blps.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Data
@Entity(name="product")
@Table(name="product")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Min(value=0)
    private float price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
