package com.example.frota.errors;

public class ManutencaoNotFoundException extends RuntimeException {
    public ManutencaoNotFoundException(Long id) {
        super("Manutenção não encontrada com ID: " + id);
    }

    public ManutencaoNotFoundException(String message) {
        super(message);
    }
}