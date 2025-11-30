package com.example.frota.errors;

public class CaixaIncompativelException extends RuntimeException {
    public CaixaIncompativelException(String message) {
        super(message);
    }

    public CaixaIncompativelException(Long caixaId, String produto, String detalhes) {
        super("A caixa " + caixaId + " não é compatível com o produto '" + produto + "': " + detalhes);
    }
}