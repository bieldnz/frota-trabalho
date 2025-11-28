package com.example.frota.manutencao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosRegistroManutencao(
    @NotNull(message = "O ID do caminhão é obrigatório")
    Long caminhaoId,
    
    @NotNull(message = "O tipo de serviço é obrigatório")
    TipoManutencao tipoServico,
    
    @NotNull(message = "A KM de realização é obrigatória")
    @Min(value = 0, message = "A KM deve ser um valor positivo")
    Double kmRealizacao,

    String observacao,
    
    @NotNull(message = "O custo é obrigatório")
    @Positive(message = "O custo deve ser positivo")
    Double custo
) {}