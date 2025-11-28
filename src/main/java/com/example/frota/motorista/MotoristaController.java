package com.example.frota.motorista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.frota.transporte.Transporte;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/motorista")
public class MotoristaController {
    
    @Autowired
    private MotoristaService motoristaService;

    // Endpoint do App do motorista para enviar coordenadas
    @PostMapping("/rastrear")
    @Transactional
    public ResponseEntity<?> atualizarRastreamento(@RequestBody @Valid DadosRastreamento dto) {
        try {
            Motorista motorista = motoristaService.atualizarRastreamento(dto);
            return ResponseEntity.ok(motorista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Endpoint do App do motorista para sinalizar entrega
    @PutMapping("/entregar/{transporteId}")
    @Transactional
    public ResponseEntity<?> sinalizarEntrega(@PathVariable Long transporteId) {
        try {
            Transporte transporte = motoristaService.sinalizarEntrega(transporteId);
            return ResponseEntity.ok(transporte);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Motorista> buscarPorId(@PathVariable Long id) {
        return motoristaService.procurarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

}