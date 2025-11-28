package com.example.frota.viagem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.frota.caminhao.Caminhao;
import com.example.frota.transporte.Transporte;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "viagem")
@Getter
@Setter
@NoArgsConstructor
public class Viagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminhao_id", referencedColumnName = "caminhao_id")
    private Caminhao caminhao;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinTable(
        name = "viagem_transporte",
        joinColumns = @JoinColumn(name = "viagem_id"),
        inverseJoinColumns = @JoinColumn(name = "transporte_id")
    )
    private List<Transporte> transportes = new ArrayList<>();

    private LocalDateTime dataHoraSaida;
    private LocalDateTime dataHoraChegada;
    
    @Column(name = "km_saida")
    private double kmSaida;
    
    @Column(name = "km_chegada")
    private double kmChegada;

    @Column(name = "total_combustivel_litros")
    private double totalCombustivelLitros = 0.0;
    
    private boolean finalizada = false;

    public Viagem(Caminhao caminhao, double kmSaida) {
        this.caminhao = caminhao;
        this.kmSaida = kmSaida;
        this.dataHoraSaida = LocalDateTime.now();
    }

    //  adicionar transportes
    public void adicionarTransporte(Transporte transporte) {
        this.transportes.add(transporte);
    }

    // Cálculo da distância percorrida
    public double getDistanciaPercorrida() {
        return kmChegada > kmSaida ? kmChegada - kmSaida : 0.0;
    }
}