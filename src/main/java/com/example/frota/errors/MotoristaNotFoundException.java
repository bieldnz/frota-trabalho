package com.example.frota.errors;

public class MotoristaNotFoundException extends RuntimeException {
    public MotoristaNotFoundException(Long id) {
        super("Motorista não encontrado com ID: " + id);
    }

    public MotoristaNotFoundException(String message) {
        super(message);
    }
}