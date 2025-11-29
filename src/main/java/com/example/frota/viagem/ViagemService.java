package com.example.frota.viagem;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoService;
import com.example.frota.transporte.Transporte;
import com.example.frota.transporte.TransporteService;
import com.example.frota.transporte.StatusEntrega;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ViagemService {

    @Autowired
    private ViagemRepository viagemRepository;
    
    @Autowired
    private CaminhaoService caminhaoService;
    
    @Autowired
    private TransporteService transporteService;

    public Viagem registrarViagem(DadosRegistroViagem dto) {
        Caminhao caminhao = caminhaoService.procurarPorId(dto.caminhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.caminhaoId()));

        if (dto.kmSaida() < caminhao.getKmAtual()) {
            throw new IllegalArgumentException("A KM de saída não pode ser menor que a KM atual do caminhão (" + caminhao.getKmAtual() + ").");
        }

        List<Transporte> transportes = transporteService.procurarPorIds(dto.transportesIds());
        if (transportes.size() != dto.transportesIds().size()) {
              throw new EntityNotFoundException("Um ou mais Transportes não foram encontrados.");
        }
                
       
        // Verifica se todos os transportes estão em status SOLICITADO e atualiza para COLETA
        for (Transporte t : transportes) {
            if (t.getStatus() != StatusEntrega.SOLICITADO) {
                throw new IllegalArgumentException("Transporte ID " + t.getId() + " não está em status SOLICITADO.");
            }
        }
        
        Viagem novaViagem = new Viagem(caminhao, dto.kmSaida());
        
        for (Transporte t : transportes) {
            novaViagem.adicionarTransporte(t);
            transporteService.atualizarStatus(t.getId(), StatusEntrega.COLETA);
        }

        // Se a Viagem for salva, o caminhão fica "em viagem".
        return viagemRepository.save(novaViagem);
    }
    
    //  Finaliza uma viagem
    public Viagem finalizarViagem(Long id, DadosFinalizacaoViagem dados) {
        Viagem viagem = viagemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viagem não encontrada com ID: " + id));

        if (viagem.isFinalizada()) {
            throw new IllegalArgumentException("Viagem já está finalizada.");
        }
        
        if (dados.kmChegada() < viagem.getKmSaida()) {
            throw new IllegalArgumentException("A KM de chegada não pode ser menor que a KM de saída.");
        }
        
        // Atualiza a Viagem
        viagem.setKmChegada(dados.kmChegada());
        viagem.setDataHoraChegada(LocalDateTime.now());
        viagem.setTotalCombustivelLitros(dados.totalCombustivelLitros());
        viagem.setFinalizada(true);
        
        // Atualiza a KM do Caminhão
        Caminhao caminhao = viagem.getCaminhao();
        caminhao.setKmAtual(dados.kmChegada());
        caminhaoService.salvarOuAtualizar(caminhao); 

        // Atualiza o status dos transportes para a próxima fase (EM_PROCESSAMENTO)
        for (Transporte t : viagem.getTransportes()) {
            if (t.getStatus() == StatusEntrega.COLETA) {
                 transporteService.atualizarStatus(t.getId(), StatusEntrega.EM_PROCESSAMENTO);
            }
        }
        
        return viagemRepository.save(viagem);
    }
    
    public List<Viagem> procurarTodos() {
        return viagemRepository.findAll();
    }
    
    public Optional<Viagem> procurarPorId(Long id) {
        return viagemRepository.findById(id);
    }
    
/*
    public void apagarPorId(Long id) {
        viagemRepository.deleteById(id);
    }

    */
}