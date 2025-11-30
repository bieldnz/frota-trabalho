package com.example.frota.errors;

public class TransportadoraNotFoundException extends RuntimeException {
    public TransportadoraNotFoundException(Long id) {
        super("Transportadora não encontrada com ID: " + id);
    }

    public TransportadoraNotFoundException(String message) {
        super(message);
    }
}
