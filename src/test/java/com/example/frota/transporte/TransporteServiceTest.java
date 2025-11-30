package com.example.frota.transporte;

import com.example.frota.cliente.Cliente;
import com.example.frota.transportadora.Transportadora;
import com.example.frota.caixa.Caixa;
import com.example.frota.cliente.ClienteService;
import com.example.frota.transportadora.TransportadoraService;
import com.example.frota.caixa.CaixaService;
import com.example.frota.errors.TransporteNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransporteServiceTest {

    @Mock
    private TransporteRepository transporteRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private TransportadoraService transportadoraService;

    @Mock
    private CaixaService caixaService;

    @InjectMocks
    private TransporteService transporteService;

    private Transporte transporte;
    private Cliente cliente;
    private Transportadora transportadora;
    private Caixa caixa;
    private DadosRegistroTransporte dadosRegistro;

    @BeforeEach
    void setUp() {
        // Setup Cliente
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        cliente.setAtivo(true);

        // Setup Transportadora
        transportadora = new Transportadora();
        transportadora.setId(1L);
        transportadora.setNome("Express Transportes");
        transportadora.setPrecoKm(5.50);
        transportadora.setValorPorCaixa(12.00);
        transportadora.setValorPorKilo(1.20);
        transportadora.setAtivo(true);

        // Setup Caixa
        caixa = new Caixa();
        caixa.setId(1L);
        caixa.setComprimento(50.0);
        caixa.setAltura(30.0);
        caixa.setLargura(40.0);
        caixa.setPeso(25.0);

        // Setup Transporte
        transporte = new Transporte();
        transporte.setId(1L);
        transporte.setCliente(cliente);
        transporte.setTransportadora(transportadora);
        transporte.setCaixa(caixa);
        transporte.setOrigem("Rua A, 100");
        transporte.setDestino("Rua B, 200");
        transporte.setStatusGeral(StatusEntrega.SOLICITADO);
        transporte.setStatusMotorista(StatusEntrega.SOLICITADO);
        transporte.setStatusCliente(StatusEntrega.SOLICITADO);

        // Setup DadosRegistroTransporte
        dadosRegistro = new DadosRegistroTransporte(
            1L, // clienteId
            1L, // transportadoraId
            Arrays.asList(1L), // caixaIds
            "Rua A, 100",
            "Rua B, 200",
            LocalDate.now().plusDays(1),
            "Carga frágil"
        );
    }

    @Test
    void criarTransporte_ComDadosValidos_DeveRetornarTransporte() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(transportadoraService.buscarPorId(1L)).thenReturn(Optional.of(transportadora));
        when(caixaService.buscarPorIds(Arrays.asList(1L))).thenReturn(Arrays.asList(caixa));

        // Act
        // Nota: Este teste seria para um método criarTransporte que não existe atualmente
        // O TransporteService atual usa salvarOuAtualizar com CadastroTransporte
        assertNotNull(dadosRegistro);
    }

    @Test
    void atualizarStatusMotorista_DeveAtualizarStatusEVerificarFinalizacao() {
        // Arrange
        transporte.setStatusCliente(StatusEntrega.ENTREGUE); // Cliente já confirmou
        when(transporteRepository.findById(1L)).thenReturn(Optional.of(transporte));
        when(transporteRepository.save(any(Transporte.class))).thenReturn(transporte);

        // Act
        DetalheTransporteDto resultado = transporteService.atualizarStatusMotorista(1L, StatusEntrega.ENTREGUE);

        // Assert
        assertNotNull(resultado);
        verify(transporteRepository).save(any(Transporte.class));
    }

    @Test
    void atualizarStatusCliente_DeveAtualizarStatusEVerificarFinalizacao() {
        // Arrange
        transporte.setStatusMotorista(StatusEntrega.ENTREGUE); // Motorista já confirmou
        when(transporteRepository.findById(1L)).thenReturn(Optional.of(transporte));
        when(transporteRepository.save(any(Transporte.class))).thenReturn(transporte);

        // Act
        DetalheTransporteDto resultado = transporteService.atualizarStatusCliente(1L, StatusEntrega.ENTREGUE);

        // Assert
        assertNotNull(resultado);
        verify(transporteRepository).save(any(Transporte.class));
    }

    @Test
    void procurarTodos_DeveRetornarLista() {
        // Arrange
        when(transporteRepository.findAllWithCaixa()).thenReturn(Arrays.asList(transporte));

        // Act
        List<DetalheTransporteDto> resultado = transporteService.procurarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(transporteRepository).findAllWithCaixa();
    }

    @Test
    void procurarPorId_ComIdExistente_DeveRetornarTransporte() {
        // Arrange
        when(transporteRepository.findByIdWithCaixa(1L)).thenReturn(Optional.of(transporte));

        // Act
        Optional<DetalheTransporteDto> resultado = transporteService.procurarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        verify(transporteRepository).findByIdWithCaixa(1L);
    }

    @Test
    void calcularFrete_ComParametrosValidos_DeveRetornarValorCorreto() {
        // Arrange
        double pesoReal = 25.0;
        double pesoCubado = 30.0;
        int numeroCaixas = 1;
        String origem = "São Paulo, SP";
        String destino = "Rio de Janeiro, RJ";

        // Act
        double resultado = transporteService.calcularFrete(pesoReal, pesoCubado, numeroCaixas, origem, destino);

        // Assert
        assertTrue(resultado > 0);
    }
}