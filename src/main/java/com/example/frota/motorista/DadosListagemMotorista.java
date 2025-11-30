package com.example.frota.motorista;

/**
 * DTO para listagem simples de motoristas
 */
public record DadosListagemMotorista(
        Long id,
        String nome,
        String cpf,
        String cnh,
        String telefoneWhatsapp,
        Boolean ativo,
        Boolean disponivel
) {
    public DadosListagemMotorista(Motorista motorista) {
        this(
            motorista.getId(),
            motorista.getNome(),
            motorista.getCpf(),
            motorista.getCnh(),
            motorista.getTelefoneWhatsapp(),
            motorista.getAtivo(),
            motorista.getDisponivel()
        );
    }
}