package com.example.frota.transportadora;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.frota.errors.TransportadoraNotFoundException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transportadoras")
public class TransportadoraController {
    
    @Autowired
    private TransportadoraService transportadoraService;
    
    /**
     * Lista todas as transportadoras com paginação
     */
    @GetMapping
    public ResponseEntity<Page<DadosListagemTransportadora>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @RequestParam(required = false) Boolean ativo) {
        
        Page<Transportadora> transportadoras = transportadoraService.listarTransportadoras(paginacao, ativo);
        Page<DadosListagemTransportadora> transportadorasDto = transportadoras.map(DadosListagemTransportadora::new);
        return ResponseEntity.ok(transportadorasDto);
    }
    
    /**
     * Busca transportadora por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadosTransportadoraDto> detalhar(@PathVariable Long id) {
        Optional<Transportadora> transportadora = transportadoraService.buscarPorId(id);
        if (transportadora.isPresent()) {
            return ResponseEntity.ok(new DadosTransportadoraDto(transportadora.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Cadastra nova transportadora
     */
    @PostMapping
    public ResponseEntity<DadosTransportadoraDto> cadastrar(
            @RequestBody @Valid DadosCadastroTransportadora dados,
            UriComponentsBuilder uriBuilder) {
        
        try {
            Transportadora transportadora = transportadoraService.cadastrarTransportadora(dados);
            URI uri = uriBuilder.path("/transportadoras/{id}").buildAndExpand(transportadora.getId()).toUri();
            return ResponseEntity.created(uri).body(new DadosTransportadoraDto(transportadora));
        } catch (TransportadoraService.CnpjJaExisteException | TransportadoraService.EmailJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza dados da transportadora
     */
    @PutMapping("/{id}")
    public ResponseEntity<DadosTransportadoraDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoTransportadora dados) {
        
        try {
            Transportadora transportadora = transportadoraService.atualizarTransportadora(id, dados);
            return ResponseEntity.ok(new DadosTransportadoraDto(transportadora));
        } catch (TransportadoraNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TransportadoraService.EmailJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza avaliação da transportadora
     */
    @PatchMapping("/{id}/avaliacao")
    public ResponseEntity<DadosTransportadoraDto> atualizarAvaliacao(
            @PathVariable Long id,
            @RequestBody @Valid DadosAvaliacaoTransportadora dados) {
        
        try {
            Transportadora transportadora = transportadoraService.atualizarAvaliacao(id, dados.avaliacao());
            return ResponseEntity.ok(new DadosTransportadoraDto(transportadora));
        } catch (TransportadoraNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Desativa transportadora (exclusão lógica)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            transportadoraService.desativarTransportadora(id);
            return ResponseEntity.noContent().build();
        } catch (TransportadoraNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Reativa transportadora
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<DadosTransportadoraDto> ativar(@PathVariable Long id) {
        try {
            Transportadora transportadora = transportadoraService.ativarTransportadora(id);
            return ResponseEntity.ok(new DadosTransportadoraDto(transportadora));
        } catch (TransportadoraNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Busca transportadoras por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<DadosListagemTransportadora>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Transportadora> transportadoras = transportadoraService.buscarPorNome(nome);
            List<DadosListagemTransportadora> transportadorasDto = transportadoras.stream()
                    .map(DadosListagemTransportadora::new)
                    .toList();
            return ResponseEntity.ok(transportadorasDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Busca transportadoras por CNPJ
     */
    @GetMapping("/buscar/cnpj")
    public ResponseEntity<List<DadosListagemTransportadora>> buscarPorCnpj(@RequestParam String cnpj) {
        try {
            List<Transportadora> transportadoras = transportadoraService.buscarPorCnpj(cnpj);
            List<DadosListagemTransportadora> transportadorasDto = transportadoras.stream()
                    .map(DadosListagemTransportadora::new)
                    .toList();
            return ResponseEntity.ok(transportadorasDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Busca transportadoras por avaliação mínima
     */
    @GetMapping("/avaliacoes")
    public ResponseEntity<List<DadosListagemTransportadora>> buscarPorAvaliacaoMinima(
            @RequestParam Double minima) {
        try {
            List<Transportadora> transportadoras = transportadoraService.buscarPorAvaliacaoMinima(minima);
            List<DadosListagemTransportadora> transportadorasDto = transportadoras.stream()
                    .map(DadosListagemTransportadora::new)
                    .toList();
            return ResponseEntity.ok(transportadorasDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Busca top transportadoras por avaliação
     */
    @GetMapping("/top")
    public ResponseEntity<List<DadosListagemTransportadora>> buscarTopTransportadoras(
            @RequestParam(defaultValue = "10") int limite) {
        
        List<Transportadora> transportadoras = transportadoraService.buscarTopTransportadoras(limite);
        List<DadosListagemTransportadora> transportadorasDto = transportadoras.stream()
                .map(DadosListagemTransportadora::new)
                .toList();
        return ResponseEntity.ok(transportadorasDto);
    }
    
    /**
     * Lista transportadoras ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<DadosListagemTransportadora>> listarAtivas() {
        List<Transportadora> transportadoras = transportadoraService.listarTransportadorasAtivas();
        List<DadosListagemTransportadora> transportadorasDto = transportadoras.stream()
                .map(DadosListagemTransportadora::new)
                .toList();
        return ResponseEntity.ok(transportadorasDto);
    }
    
    /**
     * Estatísticas de transportadoras
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<TransportadoraService.EstatisticasTransportadora> estatisticas() {
        TransportadoraService.EstatisticasTransportadora stats = transportadoraService.obterEstatisticas();
        return ResponseEntity.ok(stats);
    }
}
