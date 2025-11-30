package com.example.frota.errors;

public class CnpjJaExisteException extends RuntimeException {
    public CnpjJaExisteException(String cnpj) {
        super("CNPJ já está cadastrado no sistema: " + cnpj);
    }

    public CnpjJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }
}