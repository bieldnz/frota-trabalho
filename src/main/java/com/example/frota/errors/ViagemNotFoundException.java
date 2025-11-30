package com.example.frota.errors;

public class ViagemNotFoundException extends RuntimeException {
    public ViagemNotFoundException(Long id) {
        super("Viagem não encontrada com ID: " + id);
    }

    public ViagemNotFoundException(String message) {
        super(message);
    }
}