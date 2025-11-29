package com.example.frota.transporte;

import com.example.frota.caixa.Caixa;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
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

import java.time.LocalDateTime;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "transporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "caixa_id", referencedColumnName = "caixa_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Caixa caixa;

	String produto;

    double comprimento;
    double largura;
    double altura;
    double peso;
    int quantidade;
    String origem;
    String destino;
	double valorFrete;
    @Enumerated(EnumType.STRING)
    StatusEntrega status = StatusEntrega.SOLICITADO;
    LocalDateTime horarioRetirada;
    String statusPagamento;


    public Transporte(CadastroTransporte dto, Caixa caixa) {
        this.comprimento = dto.comprimento();
		this.produto = dto.produto();
		this.largura = dto.largura();
		this.altura = dto.altura();
		this.peso = dto.peso();
		this.quantidade = dto.quantidade();
		this.caixa = caixa;
		this.origem = dto.origem();
		this.destino = dto.destino();
        
        // Novos campos
        this.horarioRetirada = dto.horarioRetirada();
        this.statusPagamento = dto.statusPagamento();
        this.status = StatusEntrega.SOLICITADO;
    }
    
    // Método de atualização de dados, exceto o status, que é atualizado separadamente.
    public void atualizar(CadastroTransporte dto, Caixa caixa, double valorFrete) {
        this.produto = dto.produto();
        this.caixa = caixa;
        this.comprimento = dto.comprimento();
        this.largura = dto.largura();
        this.altura = dto.altura();
        this.origem = dto.origem();
        this.destino = dto.destino();
        this.peso = dto.peso();
        this.quantidade = dto.quantidade();
        this.horarioRetirada = dto.horarioRetirada();
        this.statusPagamento = dto.statusPagamento();
        this.valorFrete = valorFrete;
    }
}