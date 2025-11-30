package com.example.frota.transportadora;

/**
 * DTO resumido para listagem de transportadoras
 */
public record DadosListagemTransportadora(
        Long id,
        String nome,
        String cnpj,
        String email,
        String telefone,
        Double avaliacao,
        Boolean ativo
) {
    
    public DadosListagemTransportadora(Transportadora transportadora) {
        this(
            transportadora.getId(),
            transportadora.getNome(),
            transportadora.getCnpj(),
            transportadora.getEmail(),
            transportadora.getTelefone(),
            transportadora.getAvaliacao(),
            transportadora.getAtivo()
        );
    }
}