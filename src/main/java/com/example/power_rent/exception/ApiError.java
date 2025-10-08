package com.example.power_rent.exception;

import lombok.Getter;

@Getter
public class ApiError extends RuntimeException{
    private String message;

    public ApiError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiError(String message) {
        super(message);
    }
}
