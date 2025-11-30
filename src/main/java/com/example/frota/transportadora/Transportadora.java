package com.example.frota.transportadora;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "transportadoras")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome da transportadora é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", 
             message = "CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    @Column(name = "cnpj", unique = true, nullable = false, length = 18)
    private String cnpj;
    
    @Email(message = "Email deve ser válido")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Pattern(regexp = "^\\([1-9]{2}\\) [0-9]{4,5}-[0-9]{4}$", 
             message = "Telefone deve estar no formato (xx) xxxxx-xxxx")
    @Column(name = "telefone", length = 20)
    private String telefone;
    
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Column(name = "endereco")
    private String endereco;
    
    @Size(max = 255, message = "Observações devem ter no máximo 255 caracteres")
    @Column(name = "observacoes")
    private String observacoes;

    @DecimalMin(value = "0.0", message = "Preço por km deve ser no mínimo 0.0")
    @DecimalMax(value = "1000.0", message = "Preço por km deve ser no máximo 1000.0")
    @Column(name = "preco_km")
    private Double precoKm;
    
    @DecimalMin(value = "0.0", message = "Valor por caixa deve ser no mínimo 0.0")
    @DecimalMax(value = "1000.0", message = "Valor por caixa deve ser no máximo 1000.0")
    @Column(name = "valor_por_caixa")
    private Double valorPorCaixa;
    
    @DecimalMin(value = "0.0", message = "Valor por kilo deve ser no mínimo 0.0")
    @DecimalMax(value = "1000.0", message = "Valor por kilo deve ser no máximo 1000.0")
    @Column(name = "valor_por_kilo")
    private Double valorPorKilo;
    
    @DecimalMin(value = "0.0", message = "Avaliação deve ser no mínimo 0.0")
    @DecimalMax(value = "5.0", message = "Avaliação deve ser no máximo 5.0")
    @Column(name = "avaliacao")
    private Double avaliacao;
    
    @Builder.Default
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    public Transportadora(DadosCadastroTransportadora dto) {
        this.nome = dto.nome();
        this.cnpj = dto.cnpj();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
        this.observacoes = dto.observacoes();
        this.precoKm = dto.precoKm();
        this.valorPorCaixa = dto.valorPorCaixa();
        this.valorPorKilo = dto.valorPorKilo();
        this.ativo = true;
        this.avaliacao = 0.0;
    }

    public void atualizarInformacoes(DadosAtualizacaoTransportadora dto) {
        if (dto.nome() != null && !dto.nome().trim().isEmpty()) {
            this.nome = dto.nome().trim();
        }
        if (dto.email() != null && !dto.email().trim().isEmpty()) {
            this.email = dto.email().trim().toLowerCase();
        }
        if (dto.telefone() != null && !dto.telefone().trim().isEmpty()) {
            this.telefone = dto.telefone().trim();
        }
        if (dto.endereco() != null && !dto.endereco().trim().isEmpty()) {
            this.endereco = dto.endereco().trim();
        }
        if (dto.observacoes() != null) {
            this.observacoes = dto.observacoes().trim().isEmpty() ? null : dto.observacoes().trim();
        }
        if (dto.precoKm() != null) {
            this.precoKm = dto.precoKm();
        }
        if (dto.valorPorCaixa() != null) {
            this.valorPorCaixa = dto.valorPorCaixa();
        }
        if (dto.valorPorKilo() != null) {
            this.valorPorKilo = dto.valorPorKilo();
        }
    }

    public void desativar() {
        this.ativo = false;
    }
    
    public void ativar() {
        this.ativo = true;
    }
    
    public boolean isAtivo() {
        return this.ativo != null && this.ativo;
    }
    
    public void atualizarAvaliacao(Double novaAvaliacao) {
        if (novaAvaliacao != null && novaAvaliacao >= 0.0 && novaAvaliacao <= 5.0) {
            this.avaliacao = novaAvaliacao;
        }
    }
    
    @PrePersist
    private void prePersist() {
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
        if (this.nome != null) {
            this.nome = this.nome.trim();
        }
        if (this.cnpj != null) {
            this.cnpj = this.cnpj.trim();
        }
    }
    
    @PreUpdate
    private void preUpdate() {
        prePersist();
    }
}
