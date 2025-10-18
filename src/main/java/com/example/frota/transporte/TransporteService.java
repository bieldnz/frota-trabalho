package com.example.frota.transporte;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.frota.caixa.Caixa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

@Service
public class TransporteService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

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
}
