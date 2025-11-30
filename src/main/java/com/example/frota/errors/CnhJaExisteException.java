package com.example.frota.errors;

public class CnhJaExisteException extends RuntimeException {
    public CnhJaExisteException(String message) {
        super(message);
    }
}