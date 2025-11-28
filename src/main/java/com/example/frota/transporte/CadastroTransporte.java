package com.example.frota.transporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CadastroTransporte(
    Long id,
    @NotBlank String produto,
    double comprimento,
    double largura,
    double altura,
    Long caixaId,
    double peso,
    int quantidade,
    @NotBlank String origem,
    @NotBlank String destino,
    
    @NotNull(message = "O horário de retirada é obrigatório") 
    LocalDateTime horarioRetirada,
    
    @NotBlank(message = "O status de pagamento é obrigatório") 
    String statusPagamento 
) {
}