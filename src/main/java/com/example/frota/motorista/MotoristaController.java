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

import com.example.frota.transporte.DetalheTransporteDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/motorista")
public class MotoristaController {
    
    @Autowired
    private MotoristaService motoristaService;

    @PostMapping("/rastrear")
 
    public ResponseEntity<?> atualizarRastreamento(@RequestBody @Valid DadosRastreamento dto) {
        try {
            DetalheMotoristaDto motoristaDto = motoristaService.atualizarRastreamento(dto);
            return ResponseEntity.ok(motoristaDto);
        } catch (EntityNotFoundException e) {
             return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/entregar/{transporteId}")
     public ResponseEntity<DetalheTransporteDto> sinalizarEntrega(@PathVariable Long transporteId) {
        try {
            DetalheTransporteDto transporteDto = motoristaService.sinalizarEntrega(transporteId);
            return ResponseEntity.ok(transporteDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(null); 
        }
    }

    @GetMapping("/{id}")
public ResponseEntity<DetalheMotoristaDto> buscarPorId(@PathVariable Long id) {
    return motoristaService.procurarPorId(id)
        .map(DetalheMotoristaDto::new)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
}

}