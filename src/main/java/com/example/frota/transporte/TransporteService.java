package com.example.frota.transporte;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Import adicionado

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.example.frota.transportadora.Transportadora;
import com.example.frota.transportadora.TransportadoraRepository;
import com.example.frota.cliente.Cliente;
import com.example.frota.cliente.ClienteService;

// Imports do Logger simulado
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransporteService {
    private static final Logger logger = LoggerFactory.getLogger(TransporteService.class);

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private CaixaService caixaService;
    
    @Autowired
    private TransportadoraRepository transportadoraRepository;
    
    @Autowired
    private ClienteService clienteService;

    private static final double VALOR_POR_KM = 5.0;

    private static final double VALOR_POR_CAIXA = 10.0;

    private static final double VALOR_POR_KG = 1.0;

    private static final double FATOR_CUBAGEM = 0.3; // Corrigido: 300kg/m³ = 0.3 kg/L

    @Transactional // Transação adicionada
    public DetalheTransporteDto salvarOuAtualizar(CadastroTransporte dto) { // Retorna DTO
        Caixa caixa = caixaService.procurarPorId(dto.caixaId())
                .orElseThrow(() -> new EntityNotFoundException("Caixa não encontrada com ID: " + dto.caixaId()));
                
        Cliente cliente = clienteService.buscarPorId(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.clienteId()));
                
        Transportadora transportadora = transportadoraRepository.findById(dto.transportadoraId())
                .orElseThrow(() -> new EntityNotFoundException("Transportadora não encontrada com ID: " + dto.transportadoraId()));

        if (!caixa.cabeProduto(dto.comprimento(), dto.largura(), dto.altura(), dto.peso())) {
            throw new IllegalArgumentException("Produto não cabe na caixa.");
        }

        double valorFrete = calcularFrete(
            dto.peso(),
            (dto.comprimento() * dto.largura() * dto.altura()) * FATOR_CUBAGEM,
            dto.quantidade(),
            dto.origem(),
            dto.destino()
        );

        Transporte salvo;

        if (dto.id() != null) {
            Transporte existente = transporteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + dto.id()));
            existente.atualizar(dto, caixa, valorFrete);
            existente.setCliente(cliente);
            existente.setTransportadora(transportadora);
            salvo = transporteRepository.save(existente);
        } else {
            Transporte novo = new Transporte(dto, caixa);
            novo.setValorFrete(valorFrete);
            novo.setCliente(cliente);
            novo.setTransportadora(transportadora);
            salvo = transporteRepository.save(novo);
        }
        
        // Mapeia para DTO DENTRO da transação
        return new DetalheTransporteDto(salvo);
    }
    
    @Transactional
    public DetalheTransporteDto atualizarStatusMotorista(Long id, StatusEntrega novoStatus) {
        Transporte transporte = transporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + id));
        
        transporte.atualizarStatusMotorista(novoStatus);
        Transporte atualizado = transporteRepository.save(transporte);
        
        return new DetalheTransporteDto(atualizado);
    }
    
    @Transactional
    public DetalheTransporteDto atualizarStatusCliente(Long id, StatusEntrega novoStatus) {
        Transporte transporte = transporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + id));
        
        transporte.atualizarStatusCliente(novoStatus);
        Transporte atualizado = transporteRepository.save(transporte);
        
        return new DetalheTransporteDto(atualizado);
    }
    
    @Transactional
    public DetalheTransporteDto atualizarStatus(Long id, StatusEntrega novoStatus) {
        // Método mantido para compatibilidade - atualiza ambos os status
        Transporte transporte = transporteRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + id));
        
        transporte.atualizarStatusMotorista(novoStatus);
        transporte.atualizarStatusCliente(novoStatus);
        Transporte atualizado = transporteRepository.save(transporte);
        
        return new DetalheTransporteDto(atualizado);
    }

    // Retorna lista de DTOs e usa Fetch Join
    public List<DetalheTransporteDto> procurarTodos() {
        return transporteRepository.findAllWithCaixa().stream()
                .map(DetalheTransporteDto::new)
                .toList();
    }
    
    // Retorna lista de DTOs e usa Fetch Join
    public List<DetalheTransporteDto> procurarPorCaixaId(Long caixaId) {
        return transporteRepository.findAllByCaixaIdWithCaixa(caixaId).stream()
                .map(DetalheTransporteDto::new)
                .toList();
    }

    // Retorna Optional de DTO e usa Fetch Join
    public Optional<DetalheTransporteDto> procurarPorId(Long id) {
        return transporteRepository.findByIdWithCaixa(id)
            .map(DetalheTransporteDto::new);
    }


    public Optional<Transporte> procurarPorIdComCaixa(Long id) {
        return transporteRepository.findByIdWithCaixa(id);
    }

    public void apagarPorId(Long id) {
        transporteRepository.deleteById(id);
    }

    public double calcularFrete(double pesoReal, double pesoCubado, int numeroCaixas, String origem, String destino) {
        return calcularFrete(pesoReal, pesoCubado, numeroCaixas, origem, destino, VALOR_POR_KM, VALOR_POR_CAIXA, VALOR_POR_KG);
    }
    
    public double calcularFrete(double pesoReal, double pesoCubado, int numeroCaixas, String origem, String destino, 
                               double valorPorKm, double valorPorCaixa, double valorPorKg) {
        double pesoConsiderado = Math.max(pesoReal, pesoCubado);

        Pair<Double, Double> distanciaPedagio = calcularDistanciaPedagio(origem, destino);
        double distanciaKm = distanciaPedagio.first();
        double valorPedagio = distanciaPedagio.second();
        
        // Log para debug
        logger.info("Cálculo de frete - Peso real: {}, Peso cubado: {}, Peso considerado: {}", 
                   pesoReal, pesoCubado, pesoConsiderado);
        logger.info("Distância: {}km, Pedágio: R${}", distanciaKm, valorPedagio);
        
        // Limitar pedágio a um valor razoável (máximo R$ 100 por viagem)
        valorPedagio = Math.min(valorPedagio, 100.0);

        double valorFretePeso = (pesoConsiderado * valorPorKg) + (valorPorKm * distanciaKm) + valorPedagio;
        double valorFreteCaixa = (numeroCaixas * valorPorCaixa) + (valorPorKm * distanciaKm) + valorPedagio;
        
        double freteCalculado = Math.max(valorFretePeso, valorFreteCaixa);
        
        // Arredondar para 2 casas decimais
        BigDecimal freteArredondado = BigDecimal.valueOf(freteCalculado)
                .setScale(2, RoundingMode.HALF_UP);
        
        double freteResultado = freteArredondado.doubleValue();
        
        logger.info("Frete por peso: R${}, Frete por caixa: R${}, Frete final: R${}", 
                   valorFretePeso, valorFreteCaixa, freteResultado);
        
        return freteResultado;
    }

    public List<TransportadoraFreteDto> procurarDisponiveis(double peso, double comprimento, double largura, 
                                                           double altura, int quantidade, String origem, String destino) {
        List<Transportadora> transportadorasAtivas = transportadoraRepository.findAllActiveOrderByNome();
        
        double pesoCubado = (comprimento * largura * altura) * FATOR_CUBAGEM;
        
        return transportadorasAtivas.stream()
            .map(transportadora -> {
                double valorKm = transportadora.getPrecoKm() != null ? transportadora.getPrecoKm() : VALOR_POR_KM;
                double valorCaixa = transportadora.getValorPorCaixa() != null ? transportadora.getValorPorCaixa() : VALOR_POR_CAIXA;
                double valorKg = transportadora.getValorPorKilo() != null ? transportadora.getValorPorKilo() : VALOR_POR_KG;
                
                double valorFrete = calcularFrete(peso, pesoCubado, quantidade, origem, destino, valorKm, valorCaixa, valorKg);
                
                return new TransportadoraFreteDto(
                    transportadora.getId(),
                    transportadora.getNome(),
                    transportadora.getCnpj(),
                    transportadora.getAvaliacao(),
                    valorFrete
                );
            })
            .sorted((a, b) -> Double.compare(a.valorFrete(), b.valorFrete()))
            .toList();
    }


    public Pair<Double, Double> calcularDistanciaPedagio(String origem, String destino) {
        try {
            // Verificar se a API key está configurada
            if (googleMapsApiKey == null || googleMapsApiKey.trim().isEmpty() || googleMapsApiKey.equals("${google.maps.api.key}")) {
                logger.warn("Google Maps API key não configurada, usando valores padrão");
                return new Pair<>(50.0, 10.0); // 50km, R$10 de pedágio
            }

            GeoApiContext ctx = new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();

            DistanceMatrix matrix = DistanceMatrixApi.newRequest(ctx)
                                        .origins(origem)
                                        .destinations(destino)
                                        .mode(TravelMode.DRIVING)
                                        .await();

            if (matrix == null || matrix.rows == null || matrix.rows.length == 0
                    || matrix.rows[0].elements == null || matrix.rows[0].elements.length == 0) {
                logger.warn("Nenhuma informação retornada pela API, usando valores padrão");
                return new Pair<>(50.0, 10.0);
            }

            var element = matrix.rows[0].elements[0];
            if (element.distance == null) {
                logger.warn("Distância não disponível, usando valores padrão");
                return new Pair<>(50.0, 10.0);
            }

            double distanceKm = element.distance.inMeters / 1000.0;

            // Calcular pedágio baseado na distância (aproximadamente R$0.20 por km)
            double pedagioEstimado = distanceKm * 0.20;
            
            // Se a API retornou fare, usar o menor valor entre fare e estimativa
            double pedagioFinal = pedagioEstimado;
            if (element.fare != null) {
                double fareValue = Double.parseDouble(element.fare.value.toPlainString());
                // Usar o menor valor e limitar a R$100
                pedagioFinal = Math.min(Math.min(fareValue, pedagioEstimado), 100.0);
            }

            logger.info("Distância calculada: {}km, Pedágio: R${}", distanceKm, pedagioFinal);
            return new Pair<>(distanceKm, pedagioFinal);
            
        } catch (Exception e) {
            logger.error("Erro ao calcular distância entre {} e {}", origem, destino, e);
            // Retornar valores padrão em caso de erro
            logger.warn("Usando valores padrão devido ao erro");
            return new Pair<>(50.0, 10.0);
        }
    }

    public List<Transporte> procurarPorIds(List<Long> ids) {
        return transporteRepository.findAllByIdsWithCaixa(ids);
}

}