package com.example.frota.manutencao;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/manutencao")
public class ManutencaoController {
    
    @Autowired
    private ManutencaoService manutencaoService;

   @PostMapping
      public ResponseEntity<DadosDetalhamentoManutencao> registrarManutencao(@RequestBody @Valid DadosRegistroManutencao dto) {
        try {
            DadosDetalhamentoManutencao responseDto = manutencaoService.registrar(dto);
            
            URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(responseDto.id()) 
					.toUri();
			return ResponseEntity.created(location).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null); 
        }
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoManutencao>> listarTodos() {
        return ResponseEntity.ok(manutencaoService.procurarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoManutencao> buscarPorId(@PathVariable Long id) {
        return manutencaoService.procurarPorId(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

     @GetMapping("/alerta/{caminhaoId}")
    public ResponseEntity<String> verificarAlerta(@PathVariable Long caminhaoId) {
        try {
            String alerta = manutencaoService.verificarAlertaManutencao(caminhaoId);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao verificar alertas: " + e.getMessage());
        }
    }
}