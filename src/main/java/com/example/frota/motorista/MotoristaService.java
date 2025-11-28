package com.example.frota.motorista;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.transporte.Transporte;
import com.example.frota.viagem.Viagem;
import com.example.frota.viagem.ViagemRepository;
import com.example.frota.transporte.StatusEntrega;
import com.example.frota.transporte.TransporteService; // Adicionado para sinalizar entrega

import jakarta.persistence.EntityNotFoundException;

@Service
public class MotoristaService {
    
    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private ViagemRepository viagemRepository; 
    
    @Autowired
    private TransporteService transporteService;

    // Simulação de NotificaçãoService para WhatsApp
    private void simularNotificacaoWhatsapp(String telefone, String mensagem) {
        System.out.println("SIMULAÇÃO DE NOTIFICAÇÃO WHATSAPP para " + telefone + ": " + mensagem);
    }

    public Motorista salvar(Motorista motorista) {
        return motoristaRepository.save(motorista);
    }
    
    public Optional<Motorista> procurarPorId(Long id) {
        return motoristaRepository.findById(id);
    }

    //  Atualização de coordenadas e envio de notificação
    public Motorista atualizarRastreamento(DadosRastreamento dto) {
        Motorista motorista = motoristaRepository.findById(dto.motoristaId())
                .orElseThrow(() -> new EntityNotFoundException("Motorista não encontrado com ID: " + dto.motoristaId()));

        motorista.setLatitudeAtual(dto.latitude());
        motorista.setLongitudeAtual(dto.longitude());

        List<Viagem> viagensEmCurso = viagemRepository.findAll().stream()
                .filter(v -> !v.isFinalizada() && v.getCaminhao() != null)
                .toList();

        // 2. Verificar proximidade (simulada) e atualizar status para A_CAMINHO
        for (Viagem viagem : viagensEmCurso) {
            for (Transporte transporte : viagem.getTransportes()) {
                if (transporte.getStatus() == StatusEntrega.EM_PROCESSAMENTO) {
                     if (dto.latitude() > 0.0) { 
                        
                        // Atualiza status via TransporteService
                        transporteService.atualizarStatus(transporte.getId(), StatusEntrega.A_CAMINHO_DA_ENTREGA);

                        // Simula notificação para o destinatário
                        String mensagem = "Seu produto (" + transporte.getProduto() + ") está a caminho da entrega! Prepare-se para recebê-lo.";
                        simularNotificacaoWhatsapp(motorista.getTelefoneWhatsapp(), mensagem);
                    }
                }
            }
        }

        return motoristaRepository.save(motorista);
    }
    
 
    public Transporte sinalizarEntrega(Long transporteId) {
        Transporte transporte = transporteService.procurarPorId(transporteId)
            .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado"));

        if (transporte.getStatus() != StatusEntrega.A_CAMINHO_DA_ENTREGA) {
            throw new IllegalArgumentException("O transporte ID " + transporteId + " não está no status A CAMINHO DA ENTREGA.");
        }
        
         return transporteService.atualizarStatus(transporteId, StatusEntrega.ENTREGUE);
    }
}