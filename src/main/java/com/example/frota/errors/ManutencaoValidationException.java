package com.example.frota.errors;

public class ManutencaoValidationException extends RuntimeException {
    public ManutencaoValidationException(String message) {
        super(message);
    }

    public ManutencaoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}