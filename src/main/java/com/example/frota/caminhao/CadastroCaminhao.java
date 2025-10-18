package com.example.frota.caminhao;

import com.example.frota.marca.Marca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record CadastroCaminhao(
		@NotBlank(message = "Modelo é obrigatório")
		String modelo,
		
		@NotBlank(message = "Placa é obrigatória")
		String placa,
		
		Marca marca,
		
		@NotNull(message = "Carga máxima é obrigatória")
		@Positive(message = "Carga máxima deve ser positiva")
		double cargaMaxima,
		
		@NotNull(message = "Ano é obrigatório")
		@Min(value = 2000, message = "Ano deve ser a partir de 2000")
		int ano,

		@NotNull(message = "Comprimento é obrigatório")
		@Positive(message = "Comprimento deve ser positivo")
		double comprimento,

		@NotNull(message = "Largura é obrigatória")
		@Positive(message = "Largura deve ser positiva")
		double largura,

		@NotNull(message = "Altura é obrigatória")
		@Positive(message = "Altura deve ser positiva")
		double altura,

		@NotNull(message = "Fator de cubagem é obrigatório")
		@Positive(message = "Fator de cubagem deve ser positivo")
		double fatorCubagem
) {}

