package com.example.frota.transportadora;

import java.time.LocalDateTime;

/**
 * DTO para listagem e visualização completa de transportadoras
 */
public record DadosTransportadoraDto(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        String endereco,
        String observacoes,
        Double precoKm,
        Double valorPorCaixa,
        Double valorPorKilo,
        Double avaliacao,
        Boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    
    public DadosTransportadoraDto(Transportadora transportadora) {
        this(
            transportadora.getId(),
            transportadora.getNome(),
            transportadora.getCnpj(),
            transportadora.getEmail(),
            transportadora.getTelefone(),
            transportadora.getEndereco(),
            transportadora.getObservacoes(),
            transportadora.getPrecoKm(),
            transportadora.getValorPorCaixa(),
            transportadora.getValorPorKilo(),
            transportadora.getAvaliacao(),
            transportadora.getAtivo(),
            transportadora.getDataCriacao(),
            transportadora.getDataAtualizacao()
        );
    }
}