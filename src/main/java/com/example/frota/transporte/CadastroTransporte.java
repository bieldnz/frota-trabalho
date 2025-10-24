package com.example.frota.transporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroTransporte(
    Long id,
    @NotBlank String produto,
    @NotNull Long caminhaoId,
    Double comprimento,
    Double largura,
    Double altura,
    String material,
    Double limitePeso
) {
}
