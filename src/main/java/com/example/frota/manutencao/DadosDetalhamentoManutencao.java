package com.example.frota.manutencao;

import com.example.frota.caminhao.Caminhao;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public record DadosDetalhamentoManutencao(
    Long id,
    Long caminhaoId,
    String tipoServico,
    LocalDateTime dataRealizacao,
    Double kmRealizacao,
    String observacao,
    BigDecimal custo
) {
    public DadosDetalhamentoManutencao(Manutencao manutencao) {
        this(
            manutencao.getId(),
            manutencao.getCaminhao().getId(), 
            manutencao.getTipoServico().toString(),
            manutencao.getDataRealizacao(),
            manutencao.getKmRealizacao(),
            manutencao.getObservacao(),
            manutencao.getCusto()
        );
    }
}