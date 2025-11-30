package com.example.frota.motorista;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para cadastro de novo motorista
 */
public record DadosCadastroMotorista(
        
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve estar no formato XXX.XXX.XXX-XX")
        String cpf,
        
        @NotBlank(message = "CNH é obrigatória")
        @Size(min = 11, max = 11, message = "CNH deve ter 11 caracteres")
        String cnh,
        
        String telefoneWhatsapp
) {
}