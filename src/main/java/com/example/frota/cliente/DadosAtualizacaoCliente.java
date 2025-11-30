package com.example.frota.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para atualização de dados do cliente
 */
public record DadosAtualizacaoCliente(
        
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        String nome,
        
        @Email(message = "Email deve ser válido")
        String email,
        
        @Pattern(regexp = "^\\([1-9]{2}\\) [0-9]{4,5}-[0-9]{4}$", 
                message = "Telefone deve estar no formato (xx) xxxxx-xxxx")
        String telefone,
        
        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco,

        @Size(max = 40, message = "Cidade deve ter no máximo 40 caracteres")
        String cidade,

        @Size(max = 20, message = "Documento deve ter no máximo 20 caracteres")
        String documento,

        @Size(max = 20, message = "CEP deve ter no máximo 20 caracteres")
        String cep

) {
}