package com.example.blps.dto;

import com.example.blps.entities.Category;
import lombok.Data;

@Data
public class ProductDTO {
    private long id;

    private String name;

    private float price;

    private String category;
}
