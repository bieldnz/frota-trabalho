package com.example.frota.transporte;

import com.example.frota.caixa.Caixa;
import com.example.frota.cliente.Cliente;
import com.example.frota.transportadora.Transportadora;
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportadora_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    Transportadora transportadora;

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
    StatusEntrega statusGeral = StatusEntrega.SOLICITADO;
    
    @Enumerated(EnumType.STRING)
    StatusEntrega statusMotorista = StatusEntrega.SOLICITADO;
    
    @Enumerated(EnumType.STRING)
    StatusEntrega statusCliente = StatusEntrega.SOLICITADO;
    
    LocalDateTime horarioRetirada;
    String statusPagamento;


    public Transporte(CadastroTransporte dto, Caixa caixa, Cliente cliente, Transportadora transportadora) {
        this.comprimento = dto.comprimento();
		this.produto = dto.produto();
		this.largura = dto.largura();
		this.altura = dto.altura();
		this.peso = dto.peso();
		this.quantidade = dto.quantidade();
		this.caixa = caixa;
		this.cliente = cliente;
		this.transportadora = transportadora;
		this.origem = dto.origem();
		this.destino = dto.destino();
        this.horarioRetirada = dto.horarioRetirada();
        this.statusPagamento = dto.statusPagamento();
        this.statusGeral = StatusEntrega.SOLICITADO;
        this.statusMotorista = StatusEntrega.SOLICITADO;
        this.statusCliente = StatusEntrega.SOLICITADO;
    }
    
    // Método de atualização de dados, exceto o status, que é atualizado separadamente.
    public void atualizar(CadastroTransporte dto, Caixa caixa, Cliente cliente, Transportadora transportadora, double valorFrete) {
        this.produto = dto.produto();
        this.caixa = caixa;
        this.cliente = cliente;
        this.transportadora = transportadora;
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
    
    /**
     * Atualiza o status do motorista e verifica se deve finalizar o transporte
     */
    public void atualizarStatusMotorista(StatusEntrega novoStatus) {
        this.statusMotorista = novoStatus;
        verificarFinalizacao();
    }
    
    /**
     * Atualiza o status do cliente e verifica se deve finalizar o transporte
     */
    public void atualizarStatusCliente(StatusEntrega novoStatus) {
        this.statusCliente = novoStatus;
        verificarFinalizacao();
    }
    
    /**
     * Verifica se ambos os status são ENTREGUE para finalizar automaticamente
     */
    private void verificarFinalizacao() {
        // Garantir que os status nunca sejam null
        StatusEntrega statusMot = this.statusMotorista != null ? this.statusMotorista : StatusEntrega.SOLICITADO;
        StatusEntrega statusCli = this.statusCliente != null ? this.statusCliente : StatusEntrega.SOLICITADO;
        
        if (statusMot == StatusEntrega.ENTREGUE && statusCli == StatusEntrega.ENTREGUE) {
            this.statusGeral = StatusEntrega.FINALIZADO;
        } else {
            // Atualiza status geral com base no menor status entre motorista e cliente
            this.statusGeral = obterStatusMinimo();
        }
    }
    
    /**
     * Retorna o status mínimo entre motorista e cliente
     */
    private StatusEntrega obterStatusMinimo() {
        // Garantir que os status nunca sejam null
        StatusEntrega statusMot = this.statusMotorista != null ? this.statusMotorista : StatusEntrega.SOLICITADO;
        StatusEntrega statusCli = this.statusCliente != null ? this.statusCliente : StatusEntrega.SOLICITADO;
        
        if (statusMot.ordinal() <= statusCli.ordinal()) {
            return statusMot;
        }
        return statusCli;
    }
    
    /**
     * Define o cliente do transporte
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Define a transportadora do transporte
     */
    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }
}