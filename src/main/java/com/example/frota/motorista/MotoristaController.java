package com.example.frota.motorista;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.frota.errors.MotoristaNotFoundException;
import com.example.frota.errors.CpfJaExisteException;
import com.example.frota.errors.CnhJaExisteException;
import com.example.frota.errors.MotoristaInativoException;
import com.example.frota.transporte.DetalheTransporteDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/motorista")
public class MotoristaController {
    
    @Autowired
    private MotoristaService motoristaService;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoMotorista> cadastrar(@RequestBody @Valid DadosCadastroMotorista dados, UriComponentsBuilder uriBuilder) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.cadastrarMotorista(dados);
            var uri = uriBuilder.path("/motorista/{id}").buildAndExpand(motorista.id()).toUri();
            return ResponseEntity.created(uri).body(motorista);
        } catch (CpfJaExisteException | CnhJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoMotorista> atualizar(@RequestBody @Valid DadosAtualizacaoMotorista dados) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.atualizarMotorista(dados);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (CpfJaExisteException | CnhJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/disponibilidade")
    public ResponseEntity<DadosDetalhamentoMotorista> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.alterarDisponibilidade(id, disponivel);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MotoristaInativoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/ativo")
    public ResponseEntity<DadosDetalhamentoMotorista> alterarStatus(@PathVariable Long id, @RequestParam boolean ativo) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.alterarStatusAtivo(id, ativo);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/localizacao")
    public ResponseEntity<Void> atualizarLocalizacao(@PathVariable Long id, @RequestParam Double latitude, @RequestParam Double longitude) {
        try {
            motoristaService.atualizarLocalizacao(id, latitude, longitude);
            return ResponseEntity.ok().build();
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoMotorista> buscarPorId(@PathVariable Long id) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.buscarPorId(id);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemMotorista>> listar(@RequestParam(required = false) String status) {
        List<DadosListagemMotorista> motoristas;
        
        if ("ativos".equals(status)) {
            motoristas = motoristaService.listarAtivos();
        } else if ("disponiveis".equals(status)) {
            motoristas = motoristaService.listarDisponiveis();
        } else {
            motoristas = motoristaService.listarTodos();
        }
        
        return ResponseEntity.ok(motoristas);
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<DadosListagemMotorista>> listarAtivos(@PageableDefault(size = 10) Pageable pageable) {
        Page<DadosListagemMotorista> motoristas = motoristaService.listarAtivos(pageable);
        return ResponseEntity.ok(motoristas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<DadosListagemMotorista>> buscarPorNome(@RequestParam String nome) {
        List<DadosListagemMotorista> motoristas = motoristaService.buscarPorNome(nome);
        return ResponseEntity.ok(motoristas);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<DadosDetalhamentoMotorista> buscarPorCpf(@PathVariable String cpf) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.buscarPorCpf(cpf);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cnh/{cnh}")
    public ResponseEntity<DadosDetalhamentoMotorista> buscarPorCnh(@PathVariable String cnh) {
        try {
            DadosDetalhamentoMotorista motorista = motoristaService.buscarPorCnh(cnh);
            return ResponseEntity.ok(motorista);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/contadores/disponiveis")
    public ResponseEntity<Long> contarDisponiveis() {
        Long contador = motoristaService.contarDisponiveis();
        return ResponseEntity.ok(contador);
    }

    // Endpoints legacy para rastreamento - mantidos por compatibilidade
    @PostMapping("/rastrear")
    public ResponseEntity<DetalheMotoristaDto> atualizarRastreamento(@RequestBody @Valid DadosRastreamento dto) {
        try {
            DetalheMotoristaDto motoristaDto = motoristaService.atualizarRastreamento(dto);
            return ResponseEntity.ok(motoristaDto);
        } catch (MotoristaNotFoundException e) {
             return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/entregar/{transporteId}")
    public ResponseEntity<DetalheTransporteDto> sinalizarEntrega(@PathVariable Long transporteId) {
        try {
            DetalheTransporteDto transporteDto = motoristaService.sinalizarEntrega(transporteId);
            return ResponseEntity.ok(transporteDto);
        } catch (MotoristaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
           return ResponseEntity.badRequest().build(); 
        }
    }

    @GetMapping("/legacy/{id}")
    public ResponseEntity<DetalheMotoristaDto> buscarPorIdLegacy(@PathVariable Long id) {
        return motoristaService.procurarPorId(id)
            .map(DetalheMotoristaDto::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}