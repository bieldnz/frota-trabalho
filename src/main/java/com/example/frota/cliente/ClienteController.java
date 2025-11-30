package com.example.frota.cliente;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    /**
     * Lista todos os clientes com paginação
     */
    @GetMapping
    public ResponseEntity<Page<DadosListagemCliente>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao,
            @RequestParam(required = false) Boolean ativo) {
        
        Page<Cliente> clientes = clienteService.listarClientes(paginacao, ativo);
        Page<DadosListagemCliente> clientesDto = clientes.map(DadosListagemCliente::new);
        return ResponseEntity.ok(clientesDto);
    }
    
    /**
     * Busca cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DadosClienteDto> detalhar(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(new DadosClienteDto(cliente.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Cadastra novo cliente
     */
    @PostMapping
    public ResponseEntity<DadosClienteDto> cadastrar(
            @RequestBody @Valid DadosCadastroCliente dados,
            UriComponentsBuilder uriBuilder) {
        
        try {
            Cliente cliente = clienteService.cadastrarCliente(dados);
            URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
            return ResponseEntity.created(uri).body(new DadosClienteDto(cliente));
        } catch (ClienteService.EmailJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Atualiza dados do cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<DadosClienteDto> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoCliente dados) {
        
        try {
            Cliente cliente = clienteService.atualizarCliente(id, dados);
            return ResponseEntity.ok(new DadosClienteDto(cliente));
        } catch (ClienteService.ClienteNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (ClienteService.EmailJaExisteException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Desativa cliente (exclusão lógica)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            clienteService.desativarCliente(id);
            return ResponseEntity.noContent().build();
        } catch (ClienteService.ClienteNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Reativa cliente
     */
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<DadosClienteDto> ativar(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.ativarCliente(id);
            return ResponseEntity.ok(new DadosClienteDto(cliente));
        } catch (ClienteService.ClienteNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Busca clientes por nome
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<DadosListagemCliente>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Cliente> clientes = clienteService.buscarPorNome(nome);
            List<DadosListagemCliente> clientesDto = clientes.stream()
                    .map(DadosListagemCliente::new)
                    .toList();
            return ResponseEntity.ok(clientesDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Estatísticas de clientes
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<ClienteService.EstatisticasCliente> estatisticas() {
        ClienteService.EstatisticasCliente stats = clienteService.obterEstatisticas();
        return ResponseEntity.ok(stats);
    }
}
