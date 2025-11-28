package com.example.frota.planejamento;

import java.util.List;

import com.example.frota.transporte.Transporte;

public record CargaResumo(
    double pesoTotal,
    double volumeCubado,
    List<Transporte> transportes
) {
}