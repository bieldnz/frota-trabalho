package com.example.frota.avaliacao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.transporte.StatusEntrega;
import com.example.frota.transporte.Transporte;
import com.example.frota.transporte.TransporteService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AvaliacaoService {
    
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
    
    @Autowired
    private TransporteService transporteService;

    public Avaliacao registrarAvaliacao(DadosRegistroAvaliacao dto) {
        Transporte transporte = transporteService.procurarPorId(dto.transporteId())
            .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + dto.transporteId()));
            
        // Verificar se a entrega foi finalizada (ENTREGUE)
        if (transporte.getStatus() != StatusEntrega.ENTREGUE) {
            throw new IllegalArgumentException("A avaliação só pode ser registrada após a entrega (status: ENTREGUE). Status atual: " + transporte.getStatus());
        }
        
        //  Verificar se já existe uma avaliação
        if (avaliacaoRepository.findByTransporteId(dto.transporteId()).isPresent()) {
            throw new IllegalArgumentException("Este transporte já possui uma avaliação registrada.");
        }
        
        Avaliacao novaAvaliacao = new Avaliacao(transporte, dto);
        return avaliacaoRepository.save(novaAvaliacao);
    }
    
    public List<Avaliacao> procurarTodos() {
        return avaliacaoRepository.findAll();
    }
    
    public Optional<Avaliacao> procurarPorId(Long id) {
        return avaliacaoRepository.findById(id);
    }
}