package com.example.frota.planejamento;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoService;
import com.example.frota.transporte.Transporte;
import com.example.frota.transporte.TransporteService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PlanejamentoService {

    @Autowired
    private CaminhaoService caminhaoService;
    
    @Autowired
    private TransporteService transporteService;
    
    private static final double FATOR_CUBAGEM = 300; 

    /**
     * Calcula o peso total e o volume total para um lote de transportes.
     */
    /*public CargaResumo calcularResumoCarga(List<Long> transportesIds) {
        List<Transporte> transportes = transporteService.procurarTodos().stream()
                .filter(t -> transportesIds.contains(t.getId()))
                .toList();
        
        if (transportes.size() != transportesIds.size()) {
            throw new EntityNotFoundException("Um ou mais Transportes não foram encontrados.");
        }
        
        double pesoTotal = transportes.stream().mapToDouble(Transporte::getPeso).sum();
        double volumeTotal = transportes.stream()
            .mapToDouble(t -> t.getComprimento() * t.getLargura() * t.getAltura() * FATOR_CUBAGEM)
            .sum();

        return new CargaResumo(pesoTotal, volumeTotal, transportes);
    }*/

    public CargaResumo calcularResumoCarga(List<Long> transportesIds) {
    // CORREÇÃO: Usa o novo método para buscar transportes EFICIENTEMENTE por ID
    List<Transporte> transportes = transporteService.procurarPorIds(transportesIds);
    
    if (transportes.size() != transportesIds.size()) {
        // Se a lista retornada for menor que a lista de IDs, algo não foi encontrado
        throw new EntityNotFoundException("Um ou mais Transportes não foram encontrados.");
    }
    
    // As entidades Transporte estão corretas e prontas para o cálculo
    double pesoTotal = transportes.stream().mapToDouble(Transporte::getPeso).sum();
    double volumeTotal = transportes.stream()
        .mapToDouble(t -> t.getComprimento() * t.getLargura() * t.getAltura() * FATOR_CUBAGEM)
        .sum();

    return new CargaResumo(pesoTotal, volumeTotal, transportes);
}

    /**
     * Sugere o caminhão mais adequado para um lote de transportes com base na capacidade,
     * tentando maximizar a ocupação (minimizar o excedente).
     */
    public Caminhao sugerirMelhorCaminhao(List<Long> transportesIds) {
        CargaResumo resumo = calcularResumoCarga(transportesIds);
        List<Caminhao> todosCaminhoes = caminhaoService.procurarTodos();

        double pesoNecessario = Math.max(resumo.pesoTotal(), resumo.volumeCubado());
        
        double volumeNecessario = resumo.transportes().stream()
            .mapToDouble(t -> t.getComprimento() * t.getLargura() * t.getAltura())
            .sum();

        // 1. Filtra apenas os caminhões que suportam o peso e o volume total
        List<Caminhao> caminhoesViaveis = todosCaminhoes.stream()
            .filter(c -> c.getCargaMaxima() >= pesoNecessario)
            .filter(c -> (c.getComprimento() * c.getLargura() * c.getAltura()) >= volumeNecessario)
            .toList();

        if (caminhoesViaveis.isEmpty()) {
            throw new IllegalArgumentException("Nenhum caminhão disponível atende aos requisitos de peso ou volume para esta carga.");
        }

        // "ocupar o máximo de espaço/carga interna"
        return caminhoesViaveis.stream()
            .min(Comparator.comparingDouble(c -> c.getCargaMaxima() - pesoNecessario))
            .orElse(null); 
    }

}