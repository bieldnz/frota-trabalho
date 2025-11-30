package com.example.frota.errors;

public class CaixaNotFoundException extends RuntimeException {
    public CaixaNotFoundException(Long id) {
        super("Caixa não encontrada com ID: " + id);
    }

    public CaixaNotFoundException(String message) {
        super(message);
    }
}