package com.example.blps.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderDTO {
    private String username;

    private List<UserOrderPositionDTO> products;
}
