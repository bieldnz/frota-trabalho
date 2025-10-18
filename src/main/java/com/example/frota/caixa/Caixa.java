package com.example.frota.caixa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "caixa")
@Getter
@Setter
@EqualsAndHashCode(of ="id")
public class Caixa {
    @Id
    @GeneratedValue (strategy =GenerationType.IDENTITY)
    @Column(name = "caixa_id")
    private Long id;
    private String material;
    private double capacidadeKg;
    private boolean disponivel;
    private double altura;
    private double largura;
    private double profundidade;

    // Construtores
    public Caixa() {
    }
    public Caixa(Long id, String material, double capacidadeKg, boolean disponivel, double altura, double largura, double profundidade ) {
        this.id = id;
        this.material = material;
        this.capacidadeKg = capacidadeKg;
        this.disponivel = disponivel;
        this.altura = altura;
        this.largura = largura;
        this.profundidade = profundidade;
    }

}
