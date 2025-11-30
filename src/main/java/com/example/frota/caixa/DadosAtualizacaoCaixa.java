package com.example.frota.caixa;

import jakarta.validation.constraints.DecimalMin;

public record DadosAtualizacaoCaixa(
    @DecimalMin(value = "0.1", message = "Comprimento deve ser maior que 0")
    Double comprimento,
    
    @DecimalMin(value = "0.1", message = "Altura deve ser maior que 0")
    Double altura,
    
    @DecimalMin(value = "0.1", message = "Largura deve ser maior que 0")
    Double largura,
    
    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0")
    Double peso
) {}