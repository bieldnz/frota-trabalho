package com.example.frota.errors;

public class TransporteStatusException extends RuntimeException {
    public TransporteStatusException(String message) {
        super(message);
    }

    public TransporteStatusException(Long transporteId, String statusAtual, String statusTentado) {
        super("Não é possível alterar o status do transporte " + transporteId + 
              " de " + statusAtual + " para " + statusTentado);
    }
}