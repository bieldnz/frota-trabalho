package com.example.frota.avaliacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DadosRegistroAvaliacao(
    @NotNull(message = "O ID do transporte é obrigatório")
    Long transporteId,
    
    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    Integer nota,
    
    String comentario
) {
}