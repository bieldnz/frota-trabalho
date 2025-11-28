package com.example.frota.viagem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DadosRegistroViagem(
    @NotNull(message = "O ID do caminhão é obrigatório")
    Long caminhaoId,
    
    @NotEmpty(message = "A lista de IDs de transportes é obrigatória")
    List<Long> transportesIds,
    
    @NotNull(message = "A quilometragem de saída é obrigatória")
    @Positive(message = "A KM de saída deve ser um valor positivo")
    Double kmSaida
) {
}