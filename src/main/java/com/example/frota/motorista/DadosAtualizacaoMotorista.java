package com.example.frota.motorista;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para atualização de motorista
 */
public record DadosAtualizacaoMotorista(
        @NotNull
        Long id,
        
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,
        
        @Size(min = 11, max = 11, message = "CNH deve ter 11 caracteres")
        String cnh,
        
        String telefoneWhatsapp
) {
}