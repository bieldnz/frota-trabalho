package com.example.frota.errors;

public class AvaliacaoNotFoundException extends RuntimeException {
    public AvaliacaoNotFoundException(Long id) {
        super("Avaliação não encontrada com ID: " + id);
    }

    public AvaliacaoNotFoundException(String message) {
        super(message);
    }
}