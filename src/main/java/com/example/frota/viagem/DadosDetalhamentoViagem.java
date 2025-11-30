package com.example.frota.viagem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.frota.motorista.DadosListagemMotorista;
import com.example.frota.caminhao.Caminhao;
import com.example.frota.transporte.Transporte;

public record DadosDetalhamentoViagem(
    Long id,
    DadosListagemMotorista motorista,
    Caminhao caminhao,
    List<Long> transportesIds,
    LocalDateTime dataHoraSaida,
    LocalDateTime dataHoraChegada,
    double kmSaida,
    double kmChegada,
    double totalCombustivelLitros,
    boolean finalizada,
    double distanciaPercorrida
) {
    public DadosDetalhamentoViagem(Viagem viagem) {
        this(
            viagem.getId(),
            viagem.getMotorista() != null ? new DadosListagemMotorista(viagem.getMotorista()) : null,
            viagem.getCaminhao(),
            viagem.getTransportes().stream().map(Transporte::getId).collect(Collectors.toList()),
            viagem.getDataHoraSaida(),
            viagem.getDataHoraChegada(),
            viagem.getKmSaida(),
            viagem.getKmChegada(),
            viagem.getTotalCombustivelLitros(),
            viagem.isFinalizada(),
            viagem.getDistanciaPercorrida()
        );
    }
}