package com.example.blps.exceptions;

public class WrongStatusException extends RuntimeException{
    public WrongStatusException(String message) {
        super(message);
    }
}
