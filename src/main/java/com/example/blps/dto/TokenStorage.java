package com.example.blps.dto;

import com.example.blps.entities.AuthToken;
import lombok.Data;

import java.util.ArrayList;

@Data
public class TokenStorage {
    private ArrayList<AuthToken> tokens;
}
