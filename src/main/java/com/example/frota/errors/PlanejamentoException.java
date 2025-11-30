package com.example.frota.errors;

public class PlanejamentoException extends RuntimeException {
    public PlanejamentoException(String message) {
        super(message);
    }

    public PlanejamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}