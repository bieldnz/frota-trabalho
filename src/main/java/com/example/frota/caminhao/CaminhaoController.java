package com.example.frota.caminhao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Date;
import java.net.URI;

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

import com.example.frota.marca.MarcaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/caminhao")
public class CaminhaoController {
	
	@Autowired
	private CaminhaoService caminhaoService;
	
	@Autowired
    private CaminhaoMapper caminhaoMapper;
	
	@Autowired
	private MarcaService marcaService;

	@GetMapping                 
    public ResponseEntity<List<AtualizacaoCaminhao>> listarTodos (){ 
        List<Caminhao> caminhoes = caminhaoService.procurarTodos();
        List<AtualizacaoCaminhao> dtos = caminhoes.stream()
            .map(caminhaoMapper::toAtualizacaoDto)
            .toList();
	    return ResponseEntity.ok(dtos);             
	} 

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("=== ENDPOINT TEST CHAMADO ===");
        return ResponseEntity.ok("FUNCIONANDO!");
    }

    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> obterLogs(
        @RequestParam(required = false) Integer cod_caminhao
    ) {
        try {
            System.out.println("=== CONTROLLER: INICIO - cod_caminhao: " + cod_caminhao);
            
            // Chama o service para buscar logs reais do banco (usa método obterOperacoes)
            List<LogsCaminha> logs = caminhaoService.obterOperacoes(cod_caminhao, null, null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("cod_caminhao", cod_caminhao);
            response.put("timestamp", System.currentTimeMillis());
            response.put("logs", logs);
            response.put("total", logs.size());
            
            System.out.println("=== CONTROLLER: LOGS OBTIDOS - " + logs.size() + " registros");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Log do erro para debugging
            System.err.println("ERRO no controller: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
	@GetMapping("/{id}")
    public ResponseEntity<AtualizacaoCaminhao> buscarPorId(@PathVariable Long id) {
        try {
            Caminhao caminhao = caminhaoService.procurarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + id));
            
            AtualizacaoCaminhao dto = caminhaoMapper.toAtualizacaoDto(caminhao);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
	
	@PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody @Valid AtualizacaoCaminhao dto) {
        if (dto.id() != null) {
            return ResponseEntity.badRequest().body("Não é possível cadastrar um caminhão com ID. Use PUT para atualização.");
        }
        try{
			Caminhao caminhaoSalvo = caminhaoService.salvarOuAtualizar(dto);
			AtualizacaoCaminhao dtoSalvo = caminhaoMapper.toAtualizacaoDto(caminhaoSalvo);
            
			URI location = ServletUriComponentsBuilder
					.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(caminhaoSalvo.getId())
					.toUri();
			return ResponseEntity.created(location).body(dtoSalvo);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
    
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoCaminhao dto) {
        if (!id.equals(dto.id())) {
            return ResponseEntity.badRequest().body("O ID do caminhão na URL não corresponde ao ID no corpo da requisição.");
        }
        try {
            Caminhao caminhaoAtualizado = caminhaoService.salvarOuAtualizar(dto);
            AtualizacaoCaminhao dtoAtualizado = caminhaoMapper.toAtualizacaoDto(caminhaoAtualizado);
            return ResponseEntity.ok(dtoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
		
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		try {
			caminhaoService.apagarPorId(id);
			return ResponseEntity.noContent().build(); // Retorna 204 No Content
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Erro ao apagar caminhão com ID " + id);
		}
	}
}