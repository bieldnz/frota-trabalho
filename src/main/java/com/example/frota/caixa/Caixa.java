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
import java.time.LocalDateTime;

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
    
    // Novos campos para compatibilidade com testes
    private double comprimento; // = profundidade 
    private double peso;
    
    // Campo para testes
    private LocalDateTime dataCriacao;

    // Construtores, getters e setters
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
        this.comprimento = profundidade; // Para compatibilidade
    }
    
    // Métodos de compatibilidade
    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
        this.profundidade = comprimento; // Sincroniza
    }
    
    public double getComprimento() {
        return this.profundidade; // Retorna profundidade como comprimento
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public double getVolume() {
        return profundidade * largura * altura;
    }

    public boolean cabeProduto(double produtoComprimento, double produtoLargura, double produtoAltura, double produtoPeso) {
        return produtoComprimento <= profundidade &&
               produtoLargura <= largura &&
               produtoAltura <= altura &&
               produtoPeso <= capacidadeKg;
    }

}
