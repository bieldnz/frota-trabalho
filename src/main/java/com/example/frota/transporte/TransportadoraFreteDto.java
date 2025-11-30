package com.example.frota.transporte;

/**
 * DTO para retorno de transportadoras com frete calculado
 */
public record TransportadoraFreteDto(
        Long id,
        String nome,
        String cnpj,
        Double avaliacao,
        Double valorFrete
) {
}