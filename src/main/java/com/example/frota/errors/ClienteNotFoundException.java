package com.example.frota.errors;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado com ID: " + id);
    }

    public ClienteNotFoundException(String message) {
        super(message);
    }
}