package com.example.frota.caminhao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AtualizacaoCaminhao(
        Long id,
        @NotBlank(message = "Modelo é obrigatório") 
        String modelo,
        
        @NotBlank(message = "Placa é obrigatória")
        String placa,
        
        @NotNull(message = "Ano é obrigatório")
        @Min(value = 2000, message = "Ano deve ser a partir de 2000")
        Integer ano,
        
        @NotNull(message = "Carga máxima é obrigatória")
        @Positive(message = "Carga máxima deve ser positiva")
        Double cargaMaxima,
        
        @NotNull(message = "Marca é obrigatória")
        Long marcaId,

        @NotNull(message = "Comprimento é obrigatório")
        @Positive(message = "Comprimento deve ser positivo")
        Double comprimento,

        @NotNull(message = "Largura é obrigatória")
        @Positive(message = "Largura deve ser positiva")
        Double largura,

        @NotNull(message = "Altura é obrigatória")
        @Positive(message = "Altura deve ser positiva")
        Double altura,
        
        @NotNull(message = "Quilometragem atual é obrigatória")
        @Min(value = 0, message = "Quilometragem deve ser um valor positivo")
        Double kmAtual
) {}