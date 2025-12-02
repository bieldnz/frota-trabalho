package com.example.frota.caminhao;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogsCaminha {
    private int cod_log;
    private int cod_caminhao;
    private String alteracao;
    private Timestamp data_alt;
}
