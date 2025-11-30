package com.example.frota.cliente;

import com.example.frota.errors.ClienteNotFoundException;
import com.example.frota.errors.EmailJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;
    private DadosCadastroCliente dadosCadastro;
    private DadosAtualizacaoCliente dadosAtualizacao;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("(11) 99999-9999");
        cliente.setEndereco("Rua das Flores, 123");
        cliente.setCidade("São Paulo");
        cliente.setDocumento("123.456.789-00");
        cliente.setCep("01234-567");
        cliente.setAtivo(true);
        cliente.setDataCriacao(LocalDateTime.now());

        dadosCadastro = new DadosCadastroCliente(
            "João Silva",
            "joao@email.com",
            "(11) 99999-9999",
            "Rua das Flores, 123",
            "São Paulo",
            "123.456.789-00",
            "01234-567"
        );

        dadosAtualizacao = new DadosAtualizacaoCliente(
            "João Silva Atualizado",
            "joao.novo@email.com",
            "(11) 88888-8888",
            "Rua das Palmeiras, 456",
            "São Paulo",
            "123.456.789-00",
            "01234-567"
        );
    }

    @Test
    void criarCliente_ComDadosValidos_DeveRetornarCliente() {
        // Arrange
        when(clienteRepository.existsByEmailIgnoreCase(dadosCadastro.email())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.cadastrarCliente(dadosCadastro);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getNome(), resultado.getNome());
        assertEquals(cliente.getEmail(), resultado.getEmail());
        verify(clienteRepository).existsByEmailIgnoreCase(dadosCadastro.email());
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void criarCliente_ComEmailExistente_DeveLancarExcecao() {
        // Arrange
        when(clienteRepository.existsByEmailIgnoreCase(dadosCadastro.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailJaExisteException.class,
            () -> clienteService.cadastrarCliente(dadosCadastro));        verify(clienteRepository).existsByEmailIgnoreCase(dadosCadastro.email());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void listarClientes_ComPaginacao_DeveRetornarPaginaClientes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> pageClientes = new PageImpl<>(Arrays.asList(cliente));
        when(clienteRepository.findByAtivoTrue(pageable)).thenReturn(pageClientes);

        // Act
        Page<Cliente> resultado = clienteService.listarClientes(pageable, true);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(cliente.getId(), resultado.getContent().get(0).getId());
        verify(clienteRepository).findByAtivoTrue(pageable);
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(cliente.getId(), resultado.get().getId());
        verify(clienteRepository).findById(1L);
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornarEmpty() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(clienteRepository).findById(999L);
    }

    @Test
    void atualizarCliente_ComDadosValidos_DeveRetornarClienteAtualizado() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsByEmailIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.atualizarCliente(1L, dadosAtualizacao);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void atualizarCliente_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClienteNotFoundException.class,
            () -> clienteService.atualizarCliente(999L, dadosAtualizacao));
        
        verify(clienteRepository).findById(999L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void desativarCliente_ComIdExistente_DeveDesativarCliente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        clienteService.desativarCliente(1L);

        // Assert
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void buscarPorEmail_ComEmailExistente_DeveRetornarCliente() {
        // Arrange
        when(clienteRepository.findByEmailIgnoreCase("joao@email.com")).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorEmail("joao@email.com");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(cliente.getEmail(), resultado.get().getEmail());
        verify(clienteRepository).findByEmailIgnoreCase("joao@email.com");
    }

    @Test
    void buscarPorEmail_ComEmailInexistente_DeveRetornarEmpty() {
        // Arrange
        when(clienteRepository.findByEmailIgnoreCase("inexistente@email.com")).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorEmail("inexistente@email.com");

        // Assert
        assertFalse(resultado.isPresent());
        verify(clienteRepository).findByEmailIgnoreCase("inexistente@email.com");
    }
}