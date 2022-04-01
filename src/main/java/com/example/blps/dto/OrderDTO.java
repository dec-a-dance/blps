package com.example.blps.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDTO {
    private long id;
    private long userId;
    private List<OrderPositionDTO> products;
}
