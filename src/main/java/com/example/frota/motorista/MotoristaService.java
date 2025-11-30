package com.example.frota.motorista;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.frota.errors.MotoristaNotFoundException;
import com.example.frota.errors.CpfJaExisteException;
import com.example.frota.errors.CnhJaExisteException;
import com.example.frota.errors.MotoristaInativoException;
import com.example.frota.errors.TransporteNotFoundException;
import com.example.frota.transporte.DetalheTransporteDto;
import com.example.frota.transporte.Transporte;
import com.example.frota.viagem.Viagem;
import com.example.frota.viagem.ViagemRepository;
import com.example.frota.transporte.StatusEntrega;
import com.example.frota.transporte.TransporteService; 

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

    @Transactional
    public DadosDetalhamentoMotorista cadastrarMotorista(DadosCadastroMotorista dados) {
        // Validar se CPF já existe
        if (motoristaRepository.existsByCpf(dados.cpf())) {
            throw new CpfJaExisteException("CPF já cadastrado: " + dados.cpf());
        }

        // Validar se CNH já existe
        if (motoristaRepository.existsByCnh(dados.cnh())) {
            throw new CnhJaExisteException("CNH já cadastrada: " + dados.cnh());
        }

        Motorista motorista = Motorista.builder()
                .nome(dados.nome())
                .cpf(dados.cpf())
                .cnh(dados.cnh())
                .telefoneWhatsapp(dados.telefoneWhatsapp())
                .ativo(true)
                .disponivel(false)
                .build();

        motorista = motoristaRepository.save(motorista);
        return new DadosDetalhamentoMotorista(motorista);
    }

    @Transactional
    public DadosDetalhamentoMotorista atualizarMotorista(DadosAtualizacaoMotorista dados) {
        Motorista motorista = motoristaRepository.findById(dados.id())
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com id: " + dados.id()));

        // Validar CPF se foi alterado
        if (dados.cpf() != null && !dados.cpf().equals(motorista.getCpf())) {
            if (motoristaRepository.existsByCpfAndIdNot(dados.cpf(), dados.id())) {
                throw new CpfJaExisteException("CPF já cadastrado para outro motorista: " + dados.cpf());
            }
            motorista.setCpf(dados.cpf());
        }

        // Validar CNH se foi alterada
        if (dados.cnh() != null && !dados.cnh().equals(motorista.getCnh())) {
            if (motoristaRepository.existsByCnhAndIdNot(dados.cnh(), dados.id())) {
                throw new CnhJaExisteException("CNH já cadastrada para outro motorista: " + dados.cnh());
            }
            motorista.setCnh(dados.cnh());
        }

        // Atualizar outros campos
        if (dados.nome() != null) {
            motorista.setNome(dados.nome());
        }
        if (dados.telefoneWhatsapp() != null) {
            motorista.setTelefoneWhatsapp(dados.telefoneWhatsapp());
        }

        motorista = motoristaRepository.save(motorista);
        return new DadosDetalhamentoMotorista(motorista);
    }

    @Transactional
    public DadosDetalhamentoMotorista alterarDisponibilidade(Long id, boolean disponivel) {
        Motorista motorista = motoristaRepository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com id: " + id));

        // Só pode alterar disponibilidade se estiver ativo
        if (!motorista.getAtivo()) {
            throw new MotoristaInativoException("Não é possível alterar disponibilidade de motorista inativo");
        }

        motorista.setDisponivel(disponivel);
        motorista = motoristaRepository.save(motorista);
        return new DadosDetalhamentoMotorista(motorista);
    }

    @Transactional
    public DadosDetalhamentoMotorista alterarStatusAtivo(Long id, boolean ativo) {
        Motorista motorista = motoristaRepository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com id: " + id));

        motorista.setAtivo(ativo);
        
        // Se estiver sendo inativado, também marcar como indisponível
        if (!ativo) {
            motorista.setDisponivel(false);
        }

        motorista = motoristaRepository.save(motorista);
        return new DadosDetalhamentoMotorista(motorista);
    }

    @Transactional
    public void atualizarLocalizacao(Long id, Double latitude, Double longitude) {
        Motorista motorista = motoristaRepository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com id: " + id));

        motorista.setLatitudeAtual(latitude);
        motorista.setLongitudeAtual(longitude);
        motoristaRepository.save(motorista);
    }

    public DadosDetalhamentoMotorista buscarPorId(Long id) {
        Motorista motorista = motoristaRepository.findById(id)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com id: " + id));
        return new DadosDetalhamentoMotorista(motorista);
    }

    public List<DadosListagemMotorista> listarTodos() {
        return motoristaRepository.findAll()
                .stream()
                .map(DadosListagemMotorista::new)
                .collect(Collectors.toList());
    }

    public List<DadosListagemMotorista> listarAtivos() {
        return motoristaRepository.findByAtivoTrue()
                .stream()
                .map(DadosListagemMotorista::new)
                .collect(Collectors.toList());
    }

    public List<DadosListagemMotorista> listarDisponiveis() {
        return motoristaRepository.findByAtivoTrueAndDisponivelTrue()
                .stream()
                .map(DadosListagemMotorista::new)
                .collect(Collectors.toList());
    }

    public Page<DadosListagemMotorista> listarAtivos(Pageable pageable) {
        return motoristaRepository.findByAtivoTrue(pageable)
                .map(DadosListagemMotorista::new);
    }

    public List<DadosListagemMotorista> buscarPorNome(String nome) {
        return motoristaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(DadosListagemMotorista::new)
                .collect(Collectors.toList());
    }

    public Long contarDisponiveis() {
        return motoristaRepository.countMotoristaDisponiveis();
    }

    public DadosDetalhamentoMotorista buscarPorCpf(String cpf) {
        Motorista motorista = motoristaRepository.findByCpf(cpf)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com CPF: " + cpf));
        return new DadosDetalhamentoMotorista(motorista);
    }

    public DadosDetalhamentoMotorista buscarPorCnh(String cnh) {
        Motorista motorista = motoristaRepository.findByCnh(cnh)
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com CNH: " + cnh));
        return new DadosDetalhamentoMotorista(motorista);
    }

    // Métodos legacy para rastreamento - mantidos por compatibilidade
    @Transactional 
    public DetalheMotoristaDto atualizarRastreamento(DadosRastreamento dto) {
        Motorista motorista = motoristaRepository.findById(dto.motoristaId())
                .orElseThrow(() -> new MotoristaNotFoundException("Motorista não encontrado com ID: " + dto.motoristaId()));

        motorista.setLatitudeAtual(dto.latitude());
        motorista.setLongitudeAtual(dto.longitude());

        List<Viagem> viagensEmCurso = viagemRepository.findAll().stream()
                .filter(v -> !v.isFinalizada() && v.getCaminhao() != null)
                .toList();

        for (Viagem viagem : viagensEmCurso) {
            for (Transporte transporte : viagem.getTransportes()) {
                if (transporte.getStatusGeral() == StatusEntrega.EM_PROCESSAMENTO) {
                     if (dto.latitude() > 0.0) { 
                        transporteService.atualizarStatus(transporte.getId(), StatusEntrega.A_CAMINHO_DA_ENTREGA);
                        String mensagem = "Seu produto (" + transporte.getProduto() + ") está a caminho da entrega! Prepare-se para recebê-lo.";
                        simularNotificacaoWhatsapp(motorista.getTelefoneWhatsapp(), mensagem);
                    }
                }
            }
        }

        Motorista atualizado = motoristaRepository.save(motorista);
        return new DetalheMotoristaDto(atualizado);
    }

    @Transactional
    public DetalheTransporteDto sinalizarEntrega(Long transporteId) {
        transporteService.procurarPorId(transporteId)
            .orElseThrow(() -> new TransporteNotFoundException("Transporte não encontrado com ID: " + transporteId));

        return transporteService.atualizarStatus(transporteId, StatusEntrega.ENTREGUE);
    }

    // Métodos legacy - mantidos por compatibilidade
    public Motorista salvar(Motorista motorista) {
        return motoristaRepository.save(motorista);
    }
    
    public Optional<Motorista> procurarPorId(Long id) {
        return motoristaRepository.findById(id);
    }
}