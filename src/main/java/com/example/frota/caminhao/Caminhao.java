package com.example.frota.caminhao;

import com.example.frota.marca.Marca;

import jakarta.persistence.Column;
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

@Entity
@Table(name = "caminhao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="id")
public class Caminhao {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "caminhao_id")
	private Long id;
	private String modelo;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marca_id", referencedColumnName = "marca_id")
	private Marca marca;
	private String placa;
	private double cargaMaxima;
	private double comprimento;
	private double altura;
	private double largura;
	private int ano;
    private double kmAtual = 0.0; 

	public Caminhao(AtualizacaoCaminhao dados, Marca marca) {
		this.modelo = dados.modelo();
		this.placa = dados.placa();
		this.cargaMaxima = dados.cargaMaxima();
		this.marca = marca;
		this.ano = dados.ano();
		this.comprimento = dados.comprimento();
		this.largura = dados.largura();
		this.altura = dados.altura();
        this.kmAtual = dados.kmAtual() != null ? dados.kmAtual() : 0.0; 
	}
	
	public void atualizarInformacoes(AtualizacaoCaminhao dados, Marca marca) {
		if (dados.modelo() != null)
			this.modelo = dados.modelo();
		if (dados.placa() != null)
			this.placa = dados.placa();
		if (dados.cargaMaxima() != 0)
			this.cargaMaxima = dados.cargaMaxima();
		if (marca != null)
			this.marca = marca;
		if (dados.ano() != 0)
			this.ano = dados.ano();
		if (dados.comprimento() != 0)
			this.comprimento = dados.comprimento();
		if (dados.largura() != 0)
			this.largura = dados.largura();
		if (dados.altura() != 0)
			this.altura = dados.altura();
        if (dados.kmAtual() != null && dados.kmAtual() >= 0) // Atualiza KM se fornecido
            this.kmAtual = dados.kmAtual();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public double getCargaMaxima() {
		return cargaMaxima;
	}

	public void setCargaMaxima(double cargaMaxima) {
		this.cargaMaxima = cargaMaxima;
	}

	public double getComprimento() {
		return comprimento;
	}

	public void setComprimento(double comprimento) {
		this.comprimento = comprimento;
	}

	public double getAltura() {
		return altura;
	}

	public void setAltura(double altura) {
		this.altura = altura;
	}

	public double getLargura() {
		return largura;
	}

	public void setLargura(double largura) {
		this.largura = largura;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

    public double getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(double kmAtual) {
        this.kmAtual = kmAtual;
    }
}
