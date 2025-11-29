package com.example.frota.caixa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.DirectionsApi.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;




@RestController
@RequestMapping("/caixa")
public class CaixaController {
    
    private CaixaService caixaService;

    private CaixaRepository caixaRepository;
    private Caixa caixa;

    public CaixaController(CaixaRepository caixaRepository) {
        this.caixaRepository = caixaRepository;
    }

    @GetMapping("/capacidade")
    public ResponseEntity<?> buscarPorCapacidade(@RequestParam double capacidade) {
        List<Caixa> caixas = caixaRepository.findByCapacidadeKg(capacidade);
        if (caixas != null && !caixas.isEmpty()) {
            return ResponseEntity.ok(caixas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Caixa>> buscarTodasCaixas() {
        List<Caixa> caixas = caixaRepository.findAll();
        return ResponseEntity.ok(caixas);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@RequestParam Long id) {
        Caixa caixa = caixaRepository.findById(id).orElse(null);
        if (caixa != null) {
            return ResponseEntity.ok(caixa);
        } else {
            return ResponseEntity.notFound().build();
        }      
    }

    @PostMapping
    public ResponseEntity<Caixa> criarCaixa(@RequestBody Caixa caixa) {
        Caixa novaCaixa = caixaRepository.save(caixa);
        return ResponseEntity.ok(novaCaixa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caixa> atualizarCaixa(@PathVariable Long id, @RequestBody Caixa caixaRequest) {
        Caixa caixaExistente = caixaRepository.findById(id).orElse(null);
        if (caixaExistente != null) {
            Caixa caixaAtualizada = caixaRepository.save(caixaRequest);
            return ResponseEntity.ok(caixaAtualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    

    
    


    

    
}
