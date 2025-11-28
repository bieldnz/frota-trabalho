package com.example.frota.marca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;


@RestController 
@RequestMapping("/marca")
public class MarcaController {

	@Autowired
	private MarcaService marcaService;

	@GetMapping              
	public ResponseEntity<List<Marca>> listarTodos(){ 
		List<Marca> lista = marcaService.procurarTodos();
		return ResponseEntity.ok(lista);              
	}

	@GetMapping ("/{id}")             
	public ResponseEntity<Marca> buscarPorId(@PathVariable Long id) {
		Optional<Marca> marca = marcaService.procurarPorId(id);
		return marca.map(ResponseEntity::ok)
		             .orElseGet(() -> ResponseEntity.notFound().build());
	}
    

	@PostMapping
	@Transactional
	public ResponseEntity<Marca> cadastrar (@Valid @RequestBody DadosCadastroMarca dados) {
        Marca novaMarca = marcaService.salvar(new Marca(dados));
		return ResponseEntity.ok(novaMarca);
	}

	@PutMapping
	@Transactional
	public ResponseEntity<?> atualizar (@Valid @RequestBody DadosAtualizacaoMarca dados) { // Usa @RequestBody
        try {
            marcaService.atualizarMarca(dados);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
	}
    
    @DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> excluir (@PathVariable Long id) { 
        try {
		    marcaService.apagarPorId(id);
		    return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao apagar marca com ID: " + id);
        }
	}
}