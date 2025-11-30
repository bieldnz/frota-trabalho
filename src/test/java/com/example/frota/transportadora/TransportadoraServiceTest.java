package com.example.frota.transportadora;

import com.example.frota.errors.TransportadoraNotFoundException;
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
class TransportadoraServiceTest {

    @Mock
    private TransportadoraRepository transportadoraRepository;

    @InjectMocks
    private TransportadoraService transportadoraService;

    private Transportadora transportadora;
    private DadosCadastroTransportadora dadosCadastro;
    private DadosAtualizacaoTransportadora dadosAtualizacao;

    @BeforeEach
    void setUp() {
        transportadora = new Transportadora();
        transportadora.setId(1L);
        transportadora.setNome("Express Transportes");
        transportadora.setCnpj("12.345.678/0001-90");
        transportadora.setEmail("contato@express.com");
        transportadora.setTelefone("(11) 3333-3333");
        transportadora.setEndereco("Av. Principal, 1000");
        transportadora.setObservacoes("Transportadora especializada");
        transportadora.setPrecoKm(5.50);
        transportadora.setValorPorCaixa(12.00);
        transportadora.setValorPorKilo(1.20);
        transportadora.setAvaliacao(0.0);
        transportadora.setAtivo(true);
        transportadora.setDataCriacao(LocalDateTime.now());

        dadosCadastro = new DadosCadastroTransportadora(
            "Express Transportes",
            "12.345.678/0001-90",
            "contato@express.com",
            "(11) 3333-3333",
            "Av. Principal, 1000",
            "Transportadora especializada",
            5.50,
            12.00,
            1.20
        );

        dadosAtualizacao = new DadosAtualizacaoTransportadora(
            "Express Transportes Ltda",
            "novo@express.com",
            "(11) 4444-4444",
            "Av. Secundária, 2000",
            "Transportadora premium",
            6.00,
            15.00,
            1.50
        );
    }

    @Test
    void cadastrarTransportadora_ComDadosValidos_DeveRetornarTransportadora() {
        // Arrange
        when(transportadoraRepository.existsByCnpj(dadosCadastro.cnpj())).thenReturn(false);
        when(transportadoraRepository.existsByEmailIgnoreCase(dadosCadastro.email())).thenReturn(false);
        when(transportadoraRepository.save(any(Transportadora.class))).thenReturn(transportadora);

        // Act
        Transportadora resultado = transportadoraService.cadastrarTransportadora(dadosCadastro);

        // Assert
        assertNotNull(resultado);
        assertEquals(transportadora.getNome(), resultado.getNome());
        assertEquals(transportadora.getCnpj(), resultado.getCnpj());
        verify(transportadoraRepository).existsByCnpj(dadosCadastro.cnpj());
        verify(transportadoraRepository).existsByEmailIgnoreCase(dadosCadastro.email());
        verify(transportadoraRepository).save(any(Transportadora.class));
    }

    @Test
    void cadastrarTransportadora_ComCnpjExistente_DeveLancarExcecao() {
        // Arrange
        when(transportadoraRepository.existsByCnpj(dadosCadastro.cnpj())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> transportadoraService.cadastrarTransportadora(dadosCadastro));
        
        verify(transportadoraRepository).existsByCnpj(dadosCadastro.cnpj());
        verify(transportadoraRepository, never()).save(any(Transportadora.class));
    }

    @Test
    void cadastrarTransportadora_ComEmailExistente_DeveLancarExcecao() {
        // Arrange
        when(transportadoraRepository.existsByCnpj(dadosCadastro.cnpj())).thenReturn(false);
        when(transportadoraRepository.existsByEmailIgnoreCase(dadosCadastro.email())).thenReturn(true);

        // Act & Assert
        assertThrows(EmailJaExisteException.class,
            () -> transportadoraService.cadastrarTransportadora(dadosCadastro));
        
        verify(transportadoraRepository).existsByCnpj(dadosCadastro.cnpj());
        verify(transportadoraRepository).existsByEmailIgnoreCase(dadosCadastro.email());
        verify(transportadoraRepository, never()).save(any(Transportadora.class));
    }

    @Test
    void listarTransportadoras_ComFiltroAtivo_DeveRetornarPaginaTransportadoras() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transportadora> pageTransportadoras = new PageImpl<>(Arrays.asList(transportadora));
        when(transportadoraRepository.findByAtivoTrue(pageable)).thenReturn(pageTransportadoras);

        // Act
        Page<Transportadora> resultado = transportadoraService.listarTransportadoras(pageable, true);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(transportadora.getId(), resultado.getContent().get(0).getId());
        verify(transportadoraRepository).findByAtivoTrue(pageable);
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarTransportadora() {
        // Arrange
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));

        // Act
        Optional<Transportadora> resultado = transportadoraService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(transportadora.getId(), resultado.get().getId());
        verify(transportadoraRepository).findById(1L);
    }

    @Test
    void atualizarTransportadora_ComDadosValidos_DeveRetornarTransportadoraAtualizada() {
        // Arrange
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));
        when(transportadoraRepository.existsByEmailIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(transportadoraRepository.save(any(Transportadora.class))).thenReturn(transportadora);

        // Act
        Transportadora resultado = transportadoraService.atualizarTransportadora(1L, dadosAtualizacao);

        // Assert
        assertNotNull(resultado);
        verify(transportadoraRepository).findById(1L);
        verify(transportadoraRepository).save(any(Transportadora.class));
    }

    @Test
    void atualizarTransportadora_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(transportadoraRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransportadoraNotFoundException.class,
            () -> transportadoraService.atualizarTransportadora(999L, dadosAtualizacao));
        
        verify(transportadoraRepository).findById(999L);
        verify(transportadoraRepository, never()).save(any(Transportadora.class));
    }

    @Test
    void atualizarAvaliacao_ComValorValido_DeveAtualizarAvaliacao() {
        // Arrange
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));
        when(transportadoraRepository.save(any(Transportadora.class))).thenReturn(transportadora);

        // Act
        Transportadora resultado = transportadoraService.atualizarAvaliacao(1L, 4.5);

        // Assert
        assertNotNull(resultado);
        verify(transportadoraRepository).findById(1L);
        verify(transportadoraRepository).save(any(Transportadora.class));
    }

    @Test
    void atualizarAvaliacao_ComValorInvalido_DeveLancarExcecao() {
        // Arrange
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> transportadoraService.atualizarAvaliacao(1L, 6.0)); // Valor acima de 5.0
        
        verify(transportadoraRepository).findById(1L);
        verify(transportadoraRepository, never()).save(any(Transportadora.class));
    }

    @Test
    void desativarTransportadora_ComIdExistente_DeveDesativarTransportadora() {
        // Arrange
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));

        // Act
        transportadoraService.desativarTransportadora(1L);

        // Assert
        verify(transportadoraRepository).findById(1L);
        verify(transportadoraRepository).save(any(Transportadora.class));
    }

    @Test
    void ativarTransportadora_ComIdExistente_DeveAtivarTransportadora() {
        // Arrange
        transportadora.setAtivo(false);
        when(transportadoraRepository.findById(1L)).thenReturn(Optional.of(transportadora));
        when(transportadoraRepository.save(any(Transportadora.class))).thenReturn(transportadora);

        // Act
        Transportadora resultado = transportadoraService.ativarTransportadora(1L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.getAtivo());
        verify(transportadoraRepository).findById(1L);
        verify(transportadoraRepository).save(any(Transportadora.class));
    }

    @Test
    void buscarPorNome_ComNomeExistente_DeveRetornarLista() {
        // Arrange
        when(transportadoraRepository.findByNomeContainingIgnoreCaseAndAtivoTrue("Express"))
            .thenReturn(Arrays.asList(transportadora));

        // Act
        var resultado = transportadoraService.buscarPorNome("Express");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transportadora.getNome(), resultado.get(0).getNome());
        verify(transportadoraRepository).findByNomeContainingIgnoreCaseAndAtivoTrue("Express");
    }

    @Test
    void buscarPorCnpj_ComCnpjExistente_DeveRetornarLista() {
        // Arrange
        when(transportadoraRepository.findByCnpjContainingAndAtivoTrue("12.345.678"))
            .thenReturn(Arrays.asList(transportadora));

        // Act
        var resultado = transportadoraService.buscarPorCnpj("12.345.678");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(transportadora.getCnpj(), resultado.get(0).getCnpj());
        verify(transportadoraRepository).findByCnpjContainingAndAtivoTrue("12.345.678");
    }
}