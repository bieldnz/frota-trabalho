package com.example.frota.transporte;

import com.example.frota.caixa.Caixa;
import java.time.LocalDateTime;

public record DetalheTransporteDto(
    Long id,
    String produto,
    Long caixaId,
    Long clienteId,
    String nomeCliente,
    Long transportadoraId,
    String nomeTransportadora,
    double comprimento,
    double largura,
    double altura,
    double peso,
    int quantidade,
    String origem,
    String destino,
    double valorFrete,
    String statusGeral,
    String statusMotorista,
    String statusCliente,
    LocalDateTime horarioRetirada,
    String statusPagamento
) {
    public DetalheTransporteDto(Transporte transporte) {
        this(
            transporte.getId(),
            transporte.getProduto(),
            transporte.getCaixa() != null ? transporte.getCaixa().getId() : null,
            transporte.getCliente() != null ? transporte.getCliente().getId() : null,
            transporte.getCliente() != null ? transporte.getCliente().getNome() : null,
            transporte.getTransportadora() != null ? transporte.getTransportadora().getId() : null,
            transporte.getTransportadora() != null ? transporte.getTransportadora().getNome() : null,
            transporte.getComprimento(),
            transporte.getLargura(),
            transporte.getAltura(),
            transporte.getPeso(),
            transporte.getQuantidade(),
            transporte.getOrigem(),
            transporte.getDestino(),
            transporte.getValorFrete(),
            transporte.getStatusGeral() != null ? transporte.getStatusGeral().toString() : "PENDENTE",
            transporte.getStatusMotorista() != null ? transporte.getStatusMotorista().toString() : "PENDENTE",
            transporte.getStatusCliente() != null ? transporte.getStatusCliente().toString() : "PENDENTE",
            transporte.getHorarioRetirada(),
            transporte.getStatusPagamento()
        );
    }
}