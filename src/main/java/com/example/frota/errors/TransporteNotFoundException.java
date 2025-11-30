package com.example.frota.errors;

public class TransporteNotFoundException extends RuntimeException {
    public TransporteNotFoundException(Long id) {
        super("Transporte não encontrado com ID: " + id);
    }

    public TransporteNotFoundException(String message) {
        super(message);
    }
}