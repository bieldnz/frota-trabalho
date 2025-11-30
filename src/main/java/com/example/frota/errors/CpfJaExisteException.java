package com.example.frota.errors;

public class CpfJaExisteException extends RuntimeException {
    public CpfJaExisteException(String message) {
        super(message);
    }
}