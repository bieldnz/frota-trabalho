package com.example.frota.motorista;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "motorista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Motorista {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Column(unique = true)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
    private String cpf;
    
    @NotBlank(message = "CNH é obrigatória")
    @Size(min = 11, max = 11, message = "CNH deve ter 11 caracteres")
    private String cnh;
    
    @Column(name = "latitude_atual")
    private Double latitudeAtual;
    
    @Column(name = "longitude_atual")
    private Double longitudeAtual;
    
    @Column(name = "telefone_whatsapp")
    private String telefoneWhatsapp;
    
    @Builder.Default
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
    
    @Builder.Default
    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel = false;
}