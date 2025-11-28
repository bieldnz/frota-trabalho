package com.example.frota.viagem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosFinalizacaoViagem(
    @NotNull(message = "A quilometragem de chegada é obrigatória")
    @Positive(message = "A KM de chegada deve ser um valor positivo")
    Double kmChegada,
    
    @NotNull(message = "O total de combustível em litros é obrigatório")
    @Min(value = 0, message = "O consumo deve ser zero ou positivo")
    Double totalCombustivelLitros
) {
}