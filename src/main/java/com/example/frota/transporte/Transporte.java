package com.example.frota.transporte;

import com.example.frota.caminhao.Caminhao;
import jakarta.persistence.Entity;
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

    private String produto;

    @ManyToOne
    @JoinColumn(name = "caminhao_id")
    private Caminhao caminhao;

    private Double comprimento;
    private Double largura;
    private Double altura;
    private String material;
    private Double limitePeso;

    public Transporte(CadastroTransporte dto, Caminhao caminhao) {
        this.produto = dto.produto();
        this.caminhao = caminhao;
        this.comprimento = dto.comprimento();
        this.largura = dto.largura();
        this.altura = dto.altura();
        this.material = dto.material();
        this.limitePeso = dto.limitePeso();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public Caminhao getCaminhao() {
		return caminhao;
	}

	public void setCaminhao(Caminhao caminhao) {
		this.caminhao = caminhao;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	public Double getLargura() {
		return largura;
	}

	public void setLargura(Double largura) {
		this.largura = largura;
	}

	public Double getAltura() {
		return altura;
	}

	public void setAltura(Double altura) {
		this.altura = altura;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public Double getLimitePeso() {
		return limitePeso;
	}

	public void setLimitePeso(Double limitePeso) {
		this.limitePeso = limitePeso;
	}
    
    
    
}
