package com.example.frota.caixa;

import com.example.frota.errors.CaixaNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaixaService {
    
    @Autowired
    private CaixaRepository caixaRepository;

    public CaixaService(CaixaRepository caixaRepository) {
        this.caixaRepository = caixaRepository;
    }

    @Transactional
    public Caixa criarCaixa(DadosRegistroCaixa dados) {
        Caixa caixa = new Caixa();
        caixa.setComprimento(dados.comprimento());
        caixa.setAltura(dados.altura());
        caixa.setLargura(dados.largura());
        caixa.setPeso(dados.peso());
        caixa.setDisponivel(true);
        
        return caixaRepository.save(caixa);
    }

    public Page<Caixa> listarCaixas(Pageable pageable) {
        return caixaRepository.findAll(pageable);
    }

    public Optional<Caixa> buscarPorId(Long id) {
        return caixaRepository.findById(id);
    }

    @Transactional
    public Caixa atualizarCaixa(Long id, DadosAtualizacaoCaixa dados) {
        Caixa caixa = caixaRepository.findById(id)
            .orElseThrow(() -> new CaixaNotFoundException(id));

        if (dados.comprimento() != null) {
            caixa.setComprimento(dados.comprimento());
        }
        if (dados.altura() != null) {
            caixa.setAltura(dados.altura());
        }
        if (dados.largura() != null) {
            caixa.setLargura(dados.largura());
        }
        if (dados.peso() != null) {
            caixa.setPeso(dados.peso());
        }

        return caixaRepository.save(caixa);
    }

    @Transactional
    public void deletarCaixa(Long id) {
        if (!caixaRepository.existsById(id)) {
            throw new CaixaNotFoundException(id);
        }
        caixaRepository.deleteById(id);
    }

    public Double calcularVolume(Caixa caixa) {
        return caixa.getComprimento() * caixa.getAltura() * caixa.getLargura();
    }

    public Double calcularPesoCubico(Caixa caixa) {
        Double volume = calcularVolume(caixa);
        return volume / 6000.0; // Fator de cubagem padrão
    }

    public Double calcularPesoTaxavel(Caixa caixa) {
        Double pesoReal = caixa.getPeso();
        Double pesoCubico = calcularPesoCubico(caixa);
        return Math.max(pesoReal, pesoCubico);
    }

    public List<Caixa> buscarPorIds(List<Long> ids) {
        return caixaRepository.findByIdIn(ids);
    }

    public List<Caixa> buscarPorDimensoes(Double minComprimento, Double maxComprimento,
                                          Double minAltura, Double maxAltura,
                                          Double minLargura, Double maxLargura) {
        return caixaRepository.findByComprimentoBetweenAndAlturaBetweenAndLarguraBetween(
            minComprimento, maxComprimento, minAltura, maxAltura, minLargura, maxLargura);
    }

    public List<Caixa> buscarPorPeso(Double minPeso, Double maxPeso) {
        return caixaRepository.findByPesoBetween(minPeso, maxPeso);
    }

    // Métodos de compatibilidade com código existente
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
