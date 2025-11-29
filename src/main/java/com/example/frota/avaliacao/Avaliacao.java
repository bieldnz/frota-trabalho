package com.example.frota.avaliacao;

import com.example.frota.transporte.Transporte;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Avaliacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transporte_id", unique = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Transporte transporte;
   
    private int nota;
    
    private String comentario;
    
    @Column(name = "data_avaliacao")
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    public Avaliacao(Transporte transporte, DadosRegistroAvaliacao dto) {
        this.transporte = transporte;
        this.nota = dto.nota();
        this.comentario = dto.comentario();
    }
}