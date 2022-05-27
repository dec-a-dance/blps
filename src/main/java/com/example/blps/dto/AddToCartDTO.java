package com.example.blps.dto;

import com.example.blps.entities.AuthToken;
import com.example.blps.entities.Product;
import lombok.Data;

@Data
public class AddToCartDTO {
    private long productId;

    private long count;
}
