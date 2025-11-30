package com.example.frota.errors;

public class MarcaNotFoundException extends RuntimeException {
    public MarcaNotFoundException(Long id) {
        super("Marca não encontrada com ID: " + id);
    }

    public MarcaNotFoundException(String message) {
        super(message);
    }
}