package com.example.frota.errors;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String email) {
        super("Email já está cadastrado no sistema: " + email);
    }

    public EmailJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}