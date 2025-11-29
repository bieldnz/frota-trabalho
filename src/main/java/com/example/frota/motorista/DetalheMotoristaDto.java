package com.example.frota.motorista;

import com.example.frota.transporte.DetalheTransporteDto;
import java.util.List;

public record DetalheMotoristaDto(
    Long id,
    String nome,
    String cpf,
    Double latitudeAtual,
    Double longitudeAtual,
    String telefoneWhatsapp
) {
    public DetalheMotoristaDto(Motorista motorista) {
        this(
            motorista.getId(),
            motorista.getNome(),
            motorista.getCpf(),
            motorista.getLatitudeAtual(),
            motorista.getLongitudeAtual(),
            motorista.getTelefoneWhatsapp()
        );
    }
}