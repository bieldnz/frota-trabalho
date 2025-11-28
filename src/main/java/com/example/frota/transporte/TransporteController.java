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
import jakarta.transaction.Transactional;
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


   @GetMapping
    // Retorna Transporte (com valorFrete e status)
    public ResponseEntity<List<Transporte>> listarTransporte() {
        return ResponseEntity.ok(transporteService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transporte> buscarPorId(@PathVariable Long id) {
        Transporte existente = transporteService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Transporte não encontrado com ID: " + id));
        return ResponseEntity.ok(existente);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroTransporte dto) {
        if (dto.id() != null) {
            return ResponseEntity.badRequest().body("Não é possível cadastrar um transporte com ID. Use PUT para atualização.");
        }
        try {
            Transporte salvo = transporteService.salvarOuAtualizar(dto);

            URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(salvo.getId())
					.toUri();
            
            return ResponseEntity.created(location).body(salvo);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid CadastroTransporte dto) {
        if (!id.equals(dto.id())) {
            return ResponseEntity.badRequest().body("O ID do transporte na URL não corresponde ao ID no corpo da requisição.");
        }
        try {
            Transporte atualizado = transporteService.salvarOuAtualizar(dto);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Atualizar status do transporte
    @PutMapping("/{id}/status/{status}")
    @Transactional
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @PathVariable StatusEntrega status) {
        try {
            Transporte atualizado = transporteService.atualizarStatus(id, status);
            return ResponseEntity.ok(atualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar status: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            transporteService.apagarPorId(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao apagar transporte com ID " + id);
        }
    }
}