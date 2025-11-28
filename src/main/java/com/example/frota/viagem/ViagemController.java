package com.example.frota.viagem;

import java.net.URI;
import java.util.List;

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

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/viagem")
public class ViagemController {

    @Autowired
    private ViagemService viagemService;

    // /viagem - Lista todas as viagens
    @GetMapping
    public ResponseEntity<List<Viagem>> listarTodas() {
        return ResponseEntity.ok(viagemService.procurarTodos());
    }

    // viagem/{id} - Busca viagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<Viagem> buscarPorId(@PathVariable Long id) {
        return viagemService.procurarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // /viagem - Inicia uma nova viagem e agrupa transportes
    @PostMapping
    @Transactional
    public ResponseEntity<?> registrar(@RequestBody @Valid DadosRegistroViagem dto) {
        try {
            Viagem novaViagem = viagemService.registrarViagem(dto);
            URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(novaViagem.getId())
					.toUri();
			return ResponseEntity.created(location).body(novaViagem);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // /viagem/{id}/finalizar - Finaliza a viagem, atualiza a KM do caminhão e status dos transportes
    @PutMapping("/{id}/finalizar")
    @Transactional
    public ResponseEntity<?> finalizar(@PathVariable Long id, @RequestBody @Valid DadosFinalizacaoViagem dados) {
        try {
            Viagem viagemFinalizada = viagemService.finalizarViagem(id, dados);
            return ResponseEntity.ok(viagemFinalizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
   /*
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        try {
            viagemService.apagarPorId(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao apagar viagem com ID " + id);
        }
    }
        */
}