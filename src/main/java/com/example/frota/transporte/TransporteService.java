package com.example.frota.transporte;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.frota.caixa.Caixa;
import com.example.frota.caminhao.Caminhao;
import com.example.frota.caminhao.CaminhaoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransporteService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private CaminhaoService caminhaoService;

    private static final double VALOR_POR_KM = 10.0;

    private static final double VALOR_POR_CAIXA = 20.0;

    private static final double VALOR_POR_KG = 10.0;

    public double calcularFrete(double pesoReal, double pesoCubado, double distanciaKm, double custoPedagio, int numeroCaixas) {
        double pesoConsiderado = Math.max(pesoReal, pesoCubado);
        double valorFretePeso = (pesoConsiderado * VALOR_POR_KG) + (VALOR_POR_KM * distanciaKm) + custoPedagio;
        double valorFreteCaixa = (numeroCaixas * VALOR_POR_CAIXA) + (VALOR_POR_KM * distanciaKm) + custoPedagio;
        return Math.min(valorFretePeso, valorFreteCaixa);
    }

    public Caixa selecionarCaixa(List<Caixa> caixasDisponiveis, double produtoComprimento, double produtoLargura, double produtoAltura, double produtoPeso) {
        return caixasDisponiveis.stream()
                .filter(caixa -> caixa.cabeProduto(produtoComprimento, produtoLargura, produtoAltura, produtoPeso))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma caixa disponível para o produto."));
    }

    public double calcularDistancia(String origem, String destino) {

        try {
            GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsApiKey)
                .build();
            GeocodingResult[] response =  GeocodingApi.geocode(context,
                "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(response[0].addressComponents));
    
            // Invoke .shutdown() after your application is done making requests
            context.shutdown();
    
            return 100.0; // Exemplo de distância fixa
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao calcular distância entre " + origem + " e " + destino);
        }

    }

    public double calcularCustoPedagio(String origem, String destino) {
        //TODO: Implementar integração com API de mapas para cálculo real de custo de pedágio
        return 50.0; // Exemplo de custo fixo de pedágio
    }

    // ---------- CRUD para Transporte ----------

    public Transporte salvarOuAtualizar(CadastroTransporte dto) {
        Caminhao caminhao = caminhaoService.procurarPorId(dto.caminhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.caminhaoId()));

        if (dto.id() != null) {
            Transporte existente = transporteRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + dto.id()));
            existente.setProduto(dto.produto());
            existente.setCaminhao(caminhao);
            existente.setComprimento(dto.comprimento());
            existente.setLargura(dto.largura());
            existente.setAltura(dto.altura());
            existente.setMaterial(dto.material());
            existente.setLimitePeso(dto.limitePeso());
            return transporteRepository.save(existente);
        } else {
            Transporte novo = new Transporte(dto, caminhao);
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
}