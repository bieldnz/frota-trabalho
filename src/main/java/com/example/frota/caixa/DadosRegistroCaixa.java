package com.example.frota.caixa;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DadosRegistroCaixa(
    @NotNull(message = "Comprimento é obrigatório")
    @DecimalMin(value = "0.1", message = "Comprimento deve ser maior que 0")
    Double comprimento,
    
    @NotNull(message = "Altura é obrigatória")
    @DecimalMin(value = "0.1", message = "Altura deve ser maior que 0")
    Double altura,
    
    @NotNull(message = "Largura é obrigatória")
    @DecimalMin(value = "0.1", message = "Largura deve ser maior que 0")
    Double largura,
    
    @NotNull(message = "Peso é obrigatório")
    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0")
    Double peso
) {}