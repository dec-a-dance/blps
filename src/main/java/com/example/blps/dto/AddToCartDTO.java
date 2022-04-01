package com.example.blps.dto;

import com.example.blps.entities.AuthToken;
import com.example.blps.entities.Product;
import lombok.Data;

@Data
public class AddToCartDTO {
    private AuthToken authToken;

    private Product product;

    private long count;
}
