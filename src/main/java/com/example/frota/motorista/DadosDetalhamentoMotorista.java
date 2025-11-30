package com.example.frota.motorista;

/**
 * DTO para detalhamento de motorista
 */
public record DadosDetalhamentoMotorista(
        Long id,
        String nome,
        String cpf,
        String cnh,
        Double latitudeAtual,
        Double longitudeAtual,
        String telefoneWhatsapp,
        Boolean ativo,
        Boolean disponivel
) {
    public DadosDetalhamentoMotorista(Motorista motorista) {
        this(
            motorista.getId(),
            motorista.getNome(),
            motorista.getCpf(),
            motorista.getCnh(),
            motorista.getLatitudeAtual(),
            motorista.getLongitudeAtual(),
            motorista.getTelefoneWhatsapp(),
            motorista.getAtivo(),
            motorista.getDisponivel()
        );
    }
}