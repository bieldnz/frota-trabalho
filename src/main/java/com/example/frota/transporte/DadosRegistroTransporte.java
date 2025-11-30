package com.example.frota.transporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record DadosRegistroTransporte(
    @NotNull(message = "Cliente é obrigatório") 
    Long clienteId,
    
    @NotNull(message = "Transportadora é obrigatória") 
    Long transportadoraId,
    
    @NotNull(message = "Caixas são obrigatórias")
    List<Long> caixaIds,
    
    @NotBlank(message = "Endereço de coleta é obrigatório")
    String enderecoColeta,
    
    @NotBlank(message = "Endereço de entrega é obrigatório")
    String enderecoEntrega,
    
    @NotNull(message = "Data de coleta é obrigatória")
    LocalDate dataColeta,
    
    String observacoes
) {}