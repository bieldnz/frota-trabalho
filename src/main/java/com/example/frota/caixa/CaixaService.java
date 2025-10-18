package com.example.frota.caixa;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class CaixaService {
    private final CaixaRepository caixaRepository;
    public CaixaService(CaixaRepository caixaRepository) {
        this.caixaRepository = caixaRepository;
    }

    // Métodos de serviço para gerenciar caix

    public List<Caixa> procurarTodos() {
        return caixaRepository.findAll();
    }

    public Optional<Caixa> procurarPorId(Long id) {
        return caixaRepository.findById(id);
    }

    public Caixa salvar(Caixa novaCaixa) {
        return caixaRepository.save(novaCaixa);
    }

    public void deletar(Long id) {
        caixaRepository.deleteById(id);
    }

    public Caixa atualizar(Caixa caixaAtualizada) {
        return caixaRepository.save(caixaAtualizada);
    }

}
