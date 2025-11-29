package com.example.frota.transporte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.frota.caixa.CaixaService;

import jakarta.persistence.EntityNotFoundException;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;


@RestController 
@RequestMapping("/transporte")
public class TransporteController {

    @Autowired
    private TransporteService transporteService;

    @Autowired
    private CaixaService caixaService;

    // Retorna lista de DTOs (Corrigido)
    @GetMapping
    public ResponseEntity<List<DetalheTransporteDto>> listarTransporte() {
        return ResponseEntity.ok(transporteService.procurarTodos());
    }

    // Retorna lista de DTOs (Corrigido)
    @GetMapping("/caixa/{caixaId}")
    public ResponseEntity<List<DetalheTransporteDto>> buscarPorCaixaId(@PathVariable Long caixaId) {
        List<DetalheTransporteDto> transportes = transporteService.procurarPorCaixaId(caixaId);
        return ResponseEntity.ok(transportes);
    }

    // Retorna DTO (Corrigido)
    @GetMapping("/{id}")
    public ResponseEntity<DetalheTransporteDto> buscarPorId(@PathVariable Long id) {
        return transporteService.procurarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DetalheTransporteDto> cadastrar(@RequestBody @Valid CadastroTransporte dto) {
        if (dto.id() != null) {
            return ResponseEntity.badRequest().body(null); 
        }
        try {
            DetalheTransporteDto salvo = transporteService.salvarOuAtualizar(dto);

            URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(salvo.id()) 
					.toUri();
            
            return ResponseEntity.created(location).body(salvo);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DetalheTransporteDto> atualizar(@PathVariable Long id, @RequestBody @Valid CadastroTransporte dto) {
        if (!id.equals(dto.id())) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            DetalheTransporteDto atualizado = transporteService.salvarOuAtualizar(dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // Atualizar status do transporte
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<DetalheTransporteDto> atualizarStatus(@PathVariable Long id, @PathVariable StatusEntrega status) {
        try {
            DetalheTransporteDto atualizado = transporteService.atualizarStatus(id, status);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            transporteService.apagarPorId(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao apagar transporte com ID " + id);
        }
    }
}