package com.example.frota.planejamento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frota.caminhao.Caminhao;

import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/planejamento")
public class PlanejamentoController {

    @Autowired
    private PlanejamentoService planejamentoService;

    //  Sugere o melhor caminhão para um lote de transportes
    @PostMapping("/sugerir-caminhao")
    public ResponseEntity<?> sugerirMelhorCaminhao(@RequestBody @NotEmpty(message = "IDs de transportes são obrigatórios") List<Long> transportesIds) {
        try {
            Caminhao caminhao = planejamentoService.sugerirMelhorCaminhao(transportesIds);
            return ResponseEntity.ok(caminhao);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}