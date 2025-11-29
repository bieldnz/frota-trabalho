package com.example.frota.transporte;

import com.example.frota.caixa.Caixa;
import java.time.LocalDateTime;

public record DetalheTransporteDto(
    Long id,
    String produto,
    Long caixaId,
    double comprimento,
    double largura,
    double altura,
    double peso,
    int quantidade,
    String origem,
    String destino,
    double valorFrete,
    String status, // Representa o enum StatusEntrega
    LocalDateTime horarioRetirada,
    String statusPagamento
) {
    public DetalheTransporteDto(Transporte transporte) {
        this(
            transporte.getId(),
            transporte.getProduto(),
            // Acesso seguro, pois o Service/Repository garante o Eager Loading
            transporte.getCaixa().getId(), 
            transporte.getComprimento(),
            transporte.getLargura(),
            transporte.getAltura(),
            transporte.getPeso(),
            transporte.getQuantidade(),
            transporte.getOrigem(),
            transporte.getDestino(),
            transporte.getValorFrete(),
            transporte.getStatus().toString(),
            transporte.getHorarioRetirada(),
            transporte.getStatusPagamento()
        );
    }
}