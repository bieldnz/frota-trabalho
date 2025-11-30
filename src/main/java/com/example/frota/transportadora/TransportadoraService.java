package com.example.frota.transportadora;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.frota.errors.TransportadoraNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TransportadoraService {
    
    @Autowired
    private TransportadoraRepository transportadoraRepository;
    
    /**
     * Lista transportadoras com paginação e filtro opcional por status
     */
    public Page<Transportadora> listarTransportadoras(Pageable paginacao, Boolean ativo) {
        if (ativo != null) {
            return ativo ? transportadoraRepository.findByAtivoTrue(paginacao) 
                        : transportadoraRepository.findByAtivoFalse(paginacao);
        }
        return transportadoraRepository.findAll(paginacao);
    }
    
    /**
     * Busca transportadora por ID
     */
    public Optional<Transportadora> buscarPorId(Long id) {
        return transportadoraRepository.findById(id);
    }
    
    /**
     * Cadastra nova transportadora
     */
    @Transactional
    public Transportadora cadastrarTransportadora(DadosCadastroTransportadora dados) {
        validarCnpjUnico(dados.cnpj(), null);
        validarEmailUnico(dados.email(), null);
        
        Transportadora transportadora = new Transportadora(dados);
        return transportadoraRepository.save(transportadora);
    }
    
    /**
     * Atualiza dados da transportadora
     */
    @Transactional
    public Transportadora atualizarTransportadora(Long id, DadosAtualizacaoTransportadora dados) {
        Transportadora transportadora = buscarTransportadoraOuLancarExcecao(id);
        
        if (dados.email() != null) {
            validarEmailUnico(dados.email(), id);
        }
        
        transportadora.atualizarInformacoes(dados);
        return transportadoraRepository.save(transportadora);
    }
    
    /**
     * Atualiza avaliação da transportadora
     */
    @Transactional
    public Transportadora atualizarAvaliacao(Long id, Double avaliacao) {
        Transportadora transportadora = buscarTransportadoraOuLancarExcecao(id);
        transportadora.atualizarAvaliacao(avaliacao);
        return transportadoraRepository.save(transportadora);
    }
    
    /**
     * Desativa transportadora (exclusão lógica)
     */
    @Transactional
    public void desativarTransportadora(Long id) {
        Transportadora transportadora = buscarTransportadoraOuLancarExcecao(id);
        transportadora.desativar();
        transportadoraRepository.save(transportadora);
    }
    
    /**
     * Reativa transportadora
     */
    @Transactional
    public Transportadora ativarTransportadora(Long id) {
        Transportadora transportadora = buscarTransportadoraOuLancarExcecao(id);
        transportadora.ativar();
        return transportadoraRepository.save(transportadora);
    }
    
    /**
     * Busca transportadoras por nome (apenas ativas)
     */
    public List<Transportadora> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return transportadoraRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome.trim());
    }
    
    /**
     * Busca transportadoras por CNPJ
     */
    public List<Transportadora> buscarPorCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio");
        }
        return transportadoraRepository.findByCnpjContainingAndAtivoTrue(cnpj.trim());
    }
    
    /**
     * Busca transportadora por email
     */
    public Optional<Transportadora> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return transportadoraRepository.findByEmailIgnoreCase(email.trim());
    }
    
    /**
     * Busca transportadora por telefone
     */
    public Optional<Transportadora> buscarPorTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return Optional.empty();
        }
        return transportadoraRepository.findByTelefone(telefone.trim());
    }
    
    /**
     * Lista todas as transportadoras ativas ordenadas por nome
     */
    public List<Transportadora> listarTransportadorasAtivas() {
        return transportadoraRepository.findAllActiveOrderByNome();
    }
    
    /**
     * Busca transportadoras com avaliação mínima
     */
    public List<Transportadora> buscarPorAvaliacaoMinima(Double avaliacaoMinima) {
        if (avaliacaoMinima == null || avaliacaoMinima < 0.0 || avaliacaoMinima > 5.0) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0.0 e 5.0");
        }
        return transportadoraRepository.findByAvaliacaoGreaterThanEqualAndAtivoTrueOrderByAvaliacaoDesc(avaliacaoMinima);
    }
    
    /**
     * Busca top transportadoras por avaliação
     */
    public List<Transportadora> buscarTopTransportadoras(int limite) {
        if (limite <= 0) {
            limite = 10;
        }
        Pageable pageable = PageRequest.of(0, limite);
        return transportadoraRepository.findTopTransportadorasByAvaliacao(pageable);
    }
    
    /**
     * Retorna estatísticas de transportadoras
     */
    public EstatisticasTransportadora obterEstatisticas() {
        long totalAtivas = transportadoraRepository.countByAtivoTrue();
        long totalInativas = transportadoraRepository.countByAtivoFalse();
        long total = totalAtivas + totalInativas;
        Double mediaAvaliacao = transportadoraRepository.findAverageAvaliacaoByAtivoTrue();
        
        return new EstatisticasTransportadora(
            total, 
            totalAtivas, 
            totalInativas, 
            mediaAvaliacao != null ? mediaAvaliacao : 0.0
        );
    }
    
    /**
     * Verifica se transportadora existe e está ativa
     */
    public boolean transportadoraExisteEEstaAtiva(Long id) {
        return transportadoraRepository.findById(id)
                .map(Transportadora::isAtivo)
                .orElse(false);
    }
    
    // Métodos privados auxiliares
    
    private Transportadora buscarTransportadoraOuLancarExcecao(Long id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new TransportadoraNotFoundException(id));
    }
    
    private void validarCnpjUnico(String cnpj, Long idExcluir) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return;
        }
        
        String cnpjLimpo = cnpj.trim();
        boolean cnpjJaExiste;
        
        if (idExcluir != null) {
            cnpjJaExiste = transportadoraRepository.existsByCnpjAndIdNot(cnpjLimpo, idExcluir);
        } else {
            cnpjJaExiste = transportadoraRepository.existsByCnpj(cnpjLimpo);
        }
        
        if (cnpjJaExiste) {
            throw new CnpjJaExisteException("CNPJ já está em uso: " + cnpjLimpo);
        }
    }
    
    private void validarEmailUnico(String email, Long idExcluir) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        
        String emailLimpo = email.trim().toLowerCase();
        boolean emailJaExiste;
        
        if (idExcluir != null) {
            emailJaExiste = transportadoraRepository.existsByEmailIgnoreCaseAndIdNot(emailLimpo, idExcluir);
        } else {
            emailJaExiste = transportadoraRepository.existsByEmailIgnoreCase(emailLimpo);
        }
        
        if (emailJaExiste) {
            throw new EmailJaExisteException("Email já está em uso: " + emailLimpo);
        }
    }
    
    // Exceções customizadas
    
    public static class CnpjJaExisteException extends RuntimeException {
        public CnpjJaExisteException(String message) {
            super(message);
        }
    }
    
    public static class EmailJaExisteException extends RuntimeException {
        public EmailJaExisteException(String message) {
            super(message);
        }
    }
    
    // Record para estatísticas
    public record EstatisticasTransportadora(
            long total,
            long ativas,
            long inativas,
            double mediaAvaliacao
    ) {}
}