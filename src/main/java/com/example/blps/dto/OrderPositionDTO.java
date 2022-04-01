package com.example.blps.dto;

import lombok.Data;

@Data
public class OrderPositionDTO {
    private long id;
    private String name;
    private float price;
    private String categoryName;
}
