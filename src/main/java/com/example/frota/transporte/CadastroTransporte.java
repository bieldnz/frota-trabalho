package com.example.frota.transporte;

import jakarta.validation.constraints.NotBlank;

public record CadastroTransporte(
    Long id,
    @NotBlank String produto,
    double comprimento,
    double largura,
    double altura,
    Long caixaId,
    double peso,
    int quantidade,
    String origem,
    String destino,
    double valorFrete
) {
}
