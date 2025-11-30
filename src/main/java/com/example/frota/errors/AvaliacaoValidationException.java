package com.example.frota.errors;

public class AvaliacaoValidationException extends RuntimeException {
    public AvaliacaoValidationException(String message) {
        super(message);
    }

    public AvaliacaoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}