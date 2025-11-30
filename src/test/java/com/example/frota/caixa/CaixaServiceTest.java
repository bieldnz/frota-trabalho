package com.example.frota.caixa;

import com.example.frota.errors.CaixaNotFoundException;
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
class CaixaServiceTest {

    @Mock
    private CaixaRepository caixaRepository;

    @InjectMocks
    private CaixaService caixaService;

    private Caixa caixa;
    private DadosRegistroCaixa dadosRegistro;
    private DadosAtualizacaoCaixa dadosAtualizacao;

    @BeforeEach
    void setUp() {
        caixa = new Caixa();
        caixa.setId(1L);
        caixa.setComprimento(50.0);
        caixa.setAltura(30.0);
        caixa.setLargura(40.0);
        caixa.setPeso(25.5);
        caixa.setDataCriacao(LocalDateTime.now());

        dadosRegistro = new DadosRegistroCaixa(
            50.0, // comprimento
            30.0, // altura
            40.0, // largura
            25.5  // peso
        );

        dadosAtualizacao = new DadosAtualizacaoCaixa(
            60.0, // comprimento
            35.0, // altura
            45.0, // largura
            30.0  // peso
        );
    }

    @Test
    void criarCaixa_ComDadosValidos_DeveRetornarCaixa() {
        // Arrange
        when(caixaRepository.save(any(Caixa.class))).thenReturn(caixa);

        // Act
        Caixa resultado = caixaService.criarCaixa(dadosRegistro);

        // Assert
        assertNotNull(resultado);
        assertEquals(caixa.getComprimento(), resultado.getComprimento());
        assertEquals(caixa.getAltura(), resultado.getAltura());
        assertEquals(caixa.getLargura(), resultado.getLargura());
        assertEquals(caixa.getPeso(), resultado.getPeso());
        verify(caixaRepository).save(any(Caixa.class));
    }

    @Test
    void listarCaixas_DeveRetornarPaginaCaixas() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Caixa> pageCaixas = new PageImpl<>(Arrays.asList(caixa));
        when(caixaRepository.findAll(pageable)).thenReturn(pageCaixas);

        // Act
        Page<Caixa> resultado = caixaService.listarCaixas(pageable);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getContent().size());
        assertEquals(caixa.getId(), resultado.getContent().get(0).getId());
        verify(caixaRepository).findAll(pageable);
    }

    @Test
    void buscarPorId_ComIdExistente_DeveRetornarCaixa() {
        // Arrange
        when(caixaRepository.findById(1L)).thenReturn(Optional.of(caixa));

        // Act
        Optional<Caixa> resultado = caixaService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(caixa.getId(), resultado.get().getId());
        verify(caixaRepository).findById(1L);
    }

    @Test
    void buscarPorId_ComIdInexistente_DeveRetornarEmpty() {
        // Arrange
        when(caixaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Caixa> resultado = caixaService.buscarPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(caixaRepository).findById(999L);
    }

    @Test
    void atualizarCaixa_ComDadosValidos_DeveRetornarCaixaAtualizada() {
        // Arrange
        when(caixaRepository.findById(1L)).thenReturn(Optional.of(caixa));
        when(caixaRepository.save(any(Caixa.class))).thenReturn(caixa);

        // Act
        Caixa resultado = caixaService.atualizarCaixa(1L, dadosAtualizacao);

        // Assert
        assertNotNull(resultado);
        verify(caixaRepository).findById(1L);
        verify(caixaRepository).save(any(Caixa.class));
    }

    @Test
    void atualizarCaixa_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(caixaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CaixaNotFoundException.class,
            () -> caixaService.atualizarCaixa(999L, dadosAtualizacao));
        
        verify(caixaRepository).findById(999L);
        verify(caixaRepository, never()).save(any(Caixa.class));
    }

    @Test
    void deletarCaixa_ComIdExistente_DeveDeletarCaixa() {
        // Arrange
        when(caixaRepository.existsById(1L)).thenReturn(true);

        // Act
        caixaService.deletarCaixa(1L);

        // Assert
        verify(caixaRepository).existsById(1L);
        verify(caixaRepository).deleteById(1L);
    }

    @Test
    void deletarCaixa_ComIdInexistente_DeveLancarExcecao() {
        // Arrange
        when(caixaRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CaixaNotFoundException.class,
            () -> caixaService.deletarCaixa(999L));
        
        verify(caixaRepository).existsById(999L);
        verify(caixaRepository, never()).deleteById(anyLong());
    }

    @Test
    void calcularVolume_DeveRetornarVolumeCorreto() {
        // Act
        Double volume = caixaService.calcularVolume(caixa);

        // Assert
        Double volumeEsperado = 50.0 * 30.0 * 40.0; // comprimento * altura * largura
        assertEquals(volumeEsperado, volume);
    }

    @Test
    void calcularPesoCubico_DeveRetornarPesoCubicoCorreto() {
        // Act
        Double pesoCubico = caixaService.calcularPesoCubico(caixa);

        // Assert
        // Peso cúbico = volume (cm³) / 6000
        Double volumeEsperado = 50.0 * 30.0 * 40.0;
        Double pesoCubicoEsperado = volumeEsperado / 6000.0;
        assertEquals(pesoCubicoEsperado, pesoCubico);
    }

    @Test
    void calcularPesoTaxavel_ComPesoRealMaior_DeveRetornarPesoReal() {
        // Arrange - peso real (25.5 kg) é maior que peso cúbico (10 kg)
        
        // Act
        Double pesoTaxavel = caixaService.calcularPesoTaxavel(caixa);

        // Assert
        assertEquals(caixa.getPeso(), pesoTaxavel);
    }

    @Test
    void calcularPesoTaxavel_ComPesoCubicoMaior_DeveRetornarPesoCubico() {
        // Arrange - modificar para peso cúbico maior
        caixa.setPeso(5.0); // peso real menor
        caixa.setComprimento(100.0); // aumentar volume para peso cúbico maior
        caixa.setAltura(60.0);
        caixa.setLargura(80.0);

        // Act
        Double pesoTaxavel = caixaService.calcularPesoTaxavel(caixa);

        // Assert
        Double pesoCubicoEsperado = (100.0 * 60.0 * 80.0) / 6000.0; // 80 kg
        assertEquals(pesoCubicoEsperado, pesoTaxavel);
    }

    @Test
    void buscarPorIds_ComIdsExistentes_DeveRetornarListaCaixas() {
        // Arrange
        Caixa caixa2 = new Caixa();
        caixa2.setId(2L);
        when(caixaRepository.findByIdIn(Arrays.asList(1L, 2L)))
            .thenReturn(Arrays.asList(caixa, caixa2));

        // Act
        var resultado = caixaService.buscarPorIds(Arrays.asList(1L, 2L));

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(caixaRepository).findByIdIn(Arrays.asList(1L, 2L));
    }

    @Test
    void buscarPorDimensoes_ComFiltros_DeveRetornarListaCaixas() {
        // Arrange
        when(caixaRepository.findByComprimentoBetweenAndAlturaBetweenAndLarguraBetween(
            anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(Arrays.asList(caixa));

        // Act
        var resultado = caixaService.buscarPorDimensoes(40.0, 60.0, 25.0, 35.0, 35.0, 45.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(caixaRepository).findByComprimentoBetweenAndAlturaBetweenAndLarguraBetween(
            40.0, 60.0, 25.0, 35.0, 35.0, 45.0);
    }

    @Test
    void buscarPorPeso_ComFiltros_DeveRetornarListaCaixas() {
        // Arrange
        when(caixaRepository.findByPesoBetween(20.0, 30.0))
            .thenReturn(Arrays.asList(caixa));

        // Act
        var resultado = caixaService.buscarPorPeso(20.0, 30.0);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(caixaRepository).findByPesoBetween(20.0, 30.0);
    }
}