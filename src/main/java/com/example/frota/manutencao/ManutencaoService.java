package com.example.frota.manutencao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ManutencaoService {

    private static final double INTERVALO_OLEO_KM = 10000.0;
    private static final double INTERVALO_PNEUS_KM = 70000.0;

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private CaminhaoService caminhaoService;

    @Transactional 
    public DadosDetalhamentoManutencao registrar(DadosRegistroManutencao dto) {
        Caminhao caminhao = caminhaoService.procurarPorId(dto.caminhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.caminhaoId()));
        
        if (dto.kmRealizacao() > caminhao.getKmAtual()) {
            throw new IllegalArgumentException("A KM de manutenção (" + dto.kmRealizacao() + ") não pode ser superior à KM atual do caminhão (" + caminhao.getKmAtual() + ").");
        }

        Manutencao novaManutencao = new Manutencao(dto, caminhao);
        Manutencao salva = manutencaoRepository.save(novaManutencao);
        return new DadosDetalhamentoManutencao(salva); 
    }
    
 
    public List<DadosDetalhamentoManutencao> procurarTodos() {
        return manutencaoRepository.findAllWithCaminhao().stream()
                .map(DadosDetalhamentoManutencao::new)
                .toList();
    }
    
    public Optional<DadosDetalhamentoManutencao> procurarPorId(Long id) {
        return manutencaoRepository.findByIdWithCaminhao(id)
                .map(DadosDetalhamentoManutencao::new);
    }

    // Lógica verificar alertas
    public String verificarAlertaManutencao(Long caminhaoId) {
        Caminhao caminhao = caminhaoService.procurarPorId(caminhaoId)
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + caminhaoId));
        
        double kmAtual = caminhao.getKmAtual();
        StringBuilder alerta = new StringBuilder();

        //  Verificar Óleo, Filtros e Pastilhas
        Optional<Manutencao> ultimaOleo = manutencaoRepository.findUltimaManutencaoPorCaminhaoETipo(caminhaoId, TipoManutencao.OLEO_FILTROS_PASTILHAS);
        if (ultimaOleo.isEmpty()) {
            alerta.append(" - ALERTA: Nunca foi registrada manutenção de Óleo/Filtros/Pastilhas. Registro obrigatório! ");
        } else {
            double kmUltimaOleo = ultimaOleo.get().getKmRealizacao();
            if (kmAtual - kmUltimaOleo >= INTERVALO_OLEO_KM) {
                alerta.append(" - ALERTA URGENTE: Manutenção de Óleo/Filtros/Pastilhas devida. ");
            } else if (kmAtual - kmUltimaOleo >= INTERVALO_OLEO_KM * 0.9) { // Alerta prévio (90%)
                alerta.append(" - AVISO: Manutenção de Óleo/Filtros/Pastilhas próxima (Km a rodar: ").append(INTERVALO_OLEO_KM - (kmAtual - kmUltimaOleo)).append("). ");
            }
        }

        //  Verificar Pneus
        Optional<Manutencao> ultimaPneus = manutencaoRepository.findUltimaManutencaoPorCaminhaoETipo(caminhaoId, TipoManutencao.PNEUS);
        if (ultimaPneus.isEmpty()) {
            alerta.append(" - ALERTA: Nunca foi registrada troca de Pneus. Registro obrigatório! ");
        } else {
            double kmUltimaPneus = ultimaPneus.get().getKmRealizacao();
            if (kmAtual - kmUltimaPneus >= INTERVALO_PNEUS_KM) {
                alerta.append(" - ALERTA URGENTE: Troca de Pneus devida. ");
            } else if (kmAtual - kmUltimaPneus >= INTERVALO_PNEUS_KM * 0.9) { 
                alerta.append(" - AVISO: Troca de Pneus próxima (Km a rodar: ").append(INTERVALO_PNEUS_KM - (kmAtual - kmUltimaPneus)).append("). ");
            }
        }
        
        return alerta.length() > 0 ? alerta.toString() : "Nenhuma manutenção obrigatória pendente.";
    }
}