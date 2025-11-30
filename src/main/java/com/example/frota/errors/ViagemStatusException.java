package com.example.frota.errors;

public class ViagemStatusException extends RuntimeException {
    public ViagemStatusException(String message) {
        super(message);
    }

    public ViagemStatusException(Long viagemId, String operacao) {
        super("Não é possível realizar a operação '" + operacao + "' na viagem " + viagemId + 
              " devido ao status atual");
    }
}