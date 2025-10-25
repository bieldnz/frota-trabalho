package com.example.frota.transporte;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.frota.caixa.Caixa;
import com.example.frota.caixa.CaixaService;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransporteService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private CaixaService caixaService;

    private static final double VALOR_POR_KM = 5.0;

    private static final double VALOR_POR_CAIXA = 10.0;

    private static final double VALOR_POR_KG = 1.0;

    private static final double FATOR_CUBAGEM = 300;

    // ---------- CRUD para Transporte ----------

    public Transporte salvarOuAtualizar(CadastroTransporte dto) {
        Caixa caixa = caixaService.procurarPorId(dto.caixaId())
                .orElseThrow(() -> new EntityNotFoundException("Caixa não encontrada com ID: " + dto.caixaId()));

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

        if (dto.id() != null) {
            Transporte existente = transporteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + dto.id()));
            existente.setProduto(dto.produto());
            existente.setCaixa(caixa);
            existente.setComprimento(dto.comprimento());
            existente.setLargura(dto.largura());
            existente.setAltura(dto.altura());
            existente.setOrigem(dto.origem());
            existente.setDestino(dto.destino());
            existente.setValorFrete(valorFrete);
            existente.setPeso(dto.peso());
            return transporteRepository.save(existente);
        } else {
            Transporte novo = new Transporte(dto, caixa);
            novo.setValorFrete(valorFrete);
            return transporteRepository.save(novo);
        }
    }

    public List<Transporte> procurarTodos() {
        return transporteRepository.findAll();
    }

    public Optional<Transporte> procurarPorId(Long id) {
        return transporteRepository.findById(id);
    }

    public void apagarPorId(Long id) {
        transporteRepository.deleteById(id);
    }

    public double calcularFrete(double pesoReal, double pesoCubado, int numeroCaixas, String origem, String destino) {
        double pesoConsiderado = Math.max(pesoReal, pesoCubado);

        Pair<Double, Double> distancia = calcularDistanciaPedagio(origem, destino);

        double valorFretePeso = (pesoConsiderado * VALOR_POR_KG) + (VALOR_POR_KM * distancia.first()) + distancia.second();
        double valorFreteCaixa = (numeroCaixas * VALOR_POR_CAIXA) + (VALOR_POR_KM * distancia.first()) + distancia.second();
        return Math.max(valorFretePeso, valorFreteCaixa);
    }

    public Pair<Double, Double> calcularDistanciaPedagio(String origem, String destino) {

        try {
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
                throw new RuntimeException("Nenhuma informação de distância retornada para origem/destino.");
            }

            var element = matrix.rows[0].elements[0];
            if (element.distance == null) {
                throw new RuntimeException("Distância não disponível para a rota solicitada.");
            }

            double distanceKm = element.distance.inMeters / 1000.0;

            BigDecimal fee;

            if (element.fare == null) {
                fee = BigDecimal.ZERO;
            } else {
                fee = element.fare.value;
            }

            return new Pair<>(distanceKm, Double.parseDouble(fee.toPlainString()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao calcular distância entre " + origem + " e " + destino);
        }

    }
}