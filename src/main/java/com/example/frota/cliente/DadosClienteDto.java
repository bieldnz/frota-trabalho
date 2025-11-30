package com.example.frota.cliente;

import java.time.LocalDateTime;

/**
 * DTO para listagem e visualização de clientes
 */
public record DadosClienteDto(
        Long id,
        String nome,
        String email,
        String telefone,
        String endereco,
        Boolean ativo,
        String cidade,
        String cep,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
    
    public DadosClienteDto(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getEndereco(),
            cliente.getAtivo(),
            cliente.getCidade(),
            cliente.getCep(),
            cliente.getDataCriacao(),
            cliente.getDataAtualizacao()
        );
    }
}
