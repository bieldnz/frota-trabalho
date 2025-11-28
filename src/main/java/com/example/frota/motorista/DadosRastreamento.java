package com.example.frota.motorista;

import jakarta.validation.constraints.NotNull;

public record DadosRastreamento(
    @NotNull(message = "O ID do motorista é obrigatório")
    Long motoristaId,
    
    @NotNull(message = "A latitude é obrigatória")
    Double latitude,
    
    @NotNull(message = "A longitude é obrigatória")
    Double longitude
) {
}