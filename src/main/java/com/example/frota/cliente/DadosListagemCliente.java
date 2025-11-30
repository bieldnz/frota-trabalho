package com.example.frota.cliente;

/**
 * DTO resumido para listagem de clientes
 */
public record DadosListagemCliente(
        Long id,
        String nome,
        String email,
        String telefone,
        String cidade,
        String cep,
        Boolean ativo
) {
    
    public DadosListagemCliente(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getEmail(),
            cliente.getTelefone(),
            cliente.getCidade(),
            cliente.getCep(),
            cliente.getAtivo()
        );
    }
}