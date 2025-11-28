package com.example.frota.manutencao;

import java.time.LocalDateTime;

import com.example.frota.caminhao.Caminhao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "manutencao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Manutencao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caminhao_id", referencedColumnName = "caminhao_id")
    private Caminhao caminhao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico")
    private TipoManutencao tipoServico;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao = LocalDateTime.now();

    @Column(name = "km_realizacao")
    private double kmRealizacao; 

    private String observacao;
    
    private double custo;

    public Manutencao(DadosRegistroManutencao dto, Caminhao caminhao) {
        this.caminhao = caminhao;
        this.tipoServico = dto.tipoServico();
        this.kmRealizacao = dto.kmRealizacao();
        this.observacao = dto.observacao();
        this.custo = dto.custo();
    }
}