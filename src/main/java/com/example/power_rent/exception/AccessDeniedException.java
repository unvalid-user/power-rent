package com.example.power_rent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException() {
        super("Cannot access resource");
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
