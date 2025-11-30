package com.example.frota.cliente;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Lista clientes com paginação e filtro opcional por status
     */
    public Page<Cliente> listarClientes(Pageable paginacao, Boolean ativo) {
        if (ativo != null) {
            return ativo ? clienteRepository.findByAtivoTrue(paginacao) 
                        : clienteRepository.findByAtivoFalse(paginacao);
        }
        return clienteRepository.findAll(paginacao);
    }
    
    /**
     * Busca cliente por ID
     */
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    /**
     * Cadastra novo cliente
     */
    @Transactional
    public Cliente cadastrarCliente(DadosCadastroCliente dados) {
        validarEmailUnico(dados.email(), null);
        
        Cliente cliente = new Cliente(dados);
        return clienteRepository.save(cliente);
    }
    
    /**
     * Atualiza dados do cliente
     */
    @Transactional
    public Cliente atualizarCliente(Long id, DadosAtualizacaoCliente dados) {
        Cliente cliente = buscarClienteOuLancarExcecao(id);
        
        if (dados.email() != null) {
            validarEmailUnico(dados.email(), id);
        }
        
        cliente.atualizarInformacoes(dados);
        return clienteRepository.save(cliente);
    }
    
    /**
     * Desativa cliente (exclusão lógica)
     */
    @Transactional
    public void desativarCliente(Long id) {
        Cliente cliente = buscarClienteOuLancarExcecao(id);
        cliente.desativar();
        clienteRepository.save(cliente);
    }
    
    /**
     * Reativa cliente
     */
    @Transactional
    public Cliente ativarCliente(Long id) {
        Cliente cliente = buscarClienteOuLancarExcecao(id);
        cliente.ativar();
        return clienteRepository.save(cliente);
    }
    
    /**
     * Busca clientes por nome (apenas ativos)
     */
    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        return clienteRepository.findByNomeContainingIgnoreCaseAndAtivoTrue(nome.trim());
    }
    
    /**
     * Busca cliente por email
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return clienteRepository.findByEmailIgnoreCase(email.trim());
    }
    
    /**
     * Busca cliente por telefone
     */
    public Optional<Cliente> buscarPorTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return Optional.empty();
        }
        return clienteRepository.findByTelefone(telefone.trim());
    }
    
    /**
     * Lista todos os clientes ativos ordenados por nome
     */
    public List<Cliente> listarClientesAtivos() {
        return clienteRepository.findAllActiveOrderByNome();
    }
    
    /**
     * Retorna estatísticas de clientes
     */
    public EstatisticasCliente obterEstatisticas() {
        long totalAtivos = clienteRepository.countByAtivoTrue();
        long totalInativos = clienteRepository.countByAtivoFalse();
        long total = totalAtivos + totalInativos;
        
        return new EstatisticasCliente(total, totalAtivos, totalInativos);
    }
    
    /**
     * Verifica se cliente existe e está ativo
     */
    public boolean clienteExisteEEstaAtivo(Long id) {
        return clienteRepository.findById(id)
                .map(Cliente::isAtivo)
                .orElse(false);
    }
    
    // Métodos privados auxiliares
    
    private Cliente buscarClienteOuLancarExcecao(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com ID: " + id));
    }
    
    private void validarEmailUnico(String email, Long idExcluir) {
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        
        String emailLimpo = email.trim().toLowerCase();
        boolean emailJaExiste;
        
        if (idExcluir != null) {
            emailJaExiste = clienteRepository.existsByEmailIgnoreCaseAndIdNot(emailLimpo, idExcluir);
        } else {
            emailJaExiste = clienteRepository.existsByEmailIgnoreCase(emailLimpo);
        }
        
        if (emailJaExiste) {
            throw new EmailJaExisteException("Email já está em uso: " + emailLimpo);
        }
    }
    
    // Exceções customizadas
    
    public static class ClienteNaoEncontradoException extends RuntimeException {
        public ClienteNaoEncontradoException(String message) {
            super(message);
        }
    }
    
    public static class EmailJaExisteException extends RuntimeException {
        public EmailJaExisteException(String message) {
            super(message);
        }
    }
    
    // Record para estatísticas
    public record EstatisticasCliente(
            long total,
            long ativos,
            long inativos
    ) {}
}