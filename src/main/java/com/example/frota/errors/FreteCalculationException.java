package com.example.frota.errors;

public class FreteCalculationException extends RuntimeException {
    public FreteCalculationException(String message) {
        super(message);
    }

    public FreteCalculationException(String origem, String destino, Throwable cause) {
        super("Erro ao calcular frete entre " + origem + " e " + destino, cause);
    }
}