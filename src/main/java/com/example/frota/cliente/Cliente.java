package com.example.frota.cliente;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "clientes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Pattern(regexp = "^\\([1-9]{2}\\) [0-9]{4,5}-[0-9]{4}$", message = "Telefone deve estar no formato (xx) xxxxx-xxxx")
    @Column(name = "telefone", length = 20)
    private String telefone;
    
    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    @Column(name = "endereco")
    private String endereco;

    @Size(max = 40, message = "Cidade deve ter no máximo 40 caracteres")
    @Column(name = "cidade", length = 40)
    private String cidade;

    @Size(max = 20, message = "Documento deve ter no máximo 20 caracteres")
    @Column(name = "documento", length = 20)
    private String documento;

    @Size(max = 20, message = "CEP deve ter no máximo 20 caracteres")
    @Column(name = "cep", length = 20)
    private String cep;

    @Builder.Default
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Cliente(DadosCadastroCliente dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.telefone = dto.telefone();
        this.endereco = dto.endereco();
        this.cidade = dto.cidade();
        this.documento = dto.documento();
        this.cep = dto.cep();
        this.ativo = true;
    }

    public void atualizarInformacoes(DadosAtualizacaoCliente dto) {
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

        if (dto.cidade() != null && !dto.cidade().trim().isEmpty()) {
            this.cidade = dto.cidade().trim();
        }
        if (dto.documento() != null && !dto.documento().trim().isEmpty()) {
            this.documento = dto.documento().trim();
        }
        if (dto.cep() != null && !dto.cep().trim().isEmpty()) {
            this.cep = dto.cep().trim();
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
    
    @PrePersist
    private void prePersist() {
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
        if (this.nome != null) {
            this.nome = this.nome.trim();
        }
    }
    
    @PreUpdate
    private void preUpdate() {
        prePersist();
    }
}
