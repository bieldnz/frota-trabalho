package com.example.frota.manutencao;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record DadosRegistroManutencao(
    @NotNull(message = "O ID do caminhão é obrigatório")
    Long caminhaoId,
    
    @NotNull(message = "O tipo de serviço é obrigatório")
    TipoManutencao tipoServico,
    
    @NotNull(message = "A KM de realização é obrigatória")
    @PositiveOrZero(message = "A KM deve ser zero ou positiva")
    Double kmRealizacao,

    String observacao,
    
    @NotNull(message = "O custo é obrigatório")
    @DecimalMin(value = "0.01", message = "O custo deve ser um valor monetário positivo")
    BigDecimal custo
) {}