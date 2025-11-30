package com.example.frota.transportadora;

import jakarta.validation.constraints.*;

/**
 * DTO para atualização de dados da transportadora
 */
public record DadosAtualizacaoTransportadora(
        
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @Email(message = "Email deve ser válido")
        String email,
        
        @Pattern(regexp = "^\\([1-9]{2}\\) [0-9]{4,5}-[0-9]{4}$", 
                message = "Telefone deve estar no formato (xx) xxxxx-xxxx")
        String telefone,
        
        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco,
        
        @Size(max = 255, message = "Observações devem ter no máximo 255 caracteres")
        String observacoes,
        
        @DecimalMin(value = "0.0", message = "Preço por km deve ser no mínimo 0.0")
        @DecimalMax(value = "1000.0", message = "Preço por km deve ser no máximo 1000.0")
        Double precoKm,
        
        @DecimalMin(value = "0.0", message = "Valor por caixa deve ser no mínimo 0.0")
        @DecimalMax(value = "1000.0", message = "Valor por caixa deve ser no máximo 1000.0")
        Double valorPorCaixa,
        
        @DecimalMin(value = "0.0", message = "Valor por kilo deve ser no mínimo 0.0")
        @DecimalMax(value = "1000.0", message = "Valor por kilo deve ser no máximo 1000.0")
        Double valorPorKilo
) {
}