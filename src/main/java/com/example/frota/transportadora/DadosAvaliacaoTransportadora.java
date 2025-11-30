package com.example.frota.transportadora;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO para atualização de avaliação da transportadora
 */
public record DadosAvaliacaoTransportadora(
        
        @DecimalMin(value = "0.0", message = "Avaliação deve ser no mínimo 0.0")
        @DecimalMax(value = "5.0", message = "Avaliação deve ser no máximo 5.0")
        Double avaliacao
) {
}