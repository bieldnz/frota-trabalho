package com.example.frota.caixa;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Transactional
@Repository

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    List<Caixa> findByCapacidadeKg(double capacidadeKg);
    
    // Métodos para compatibilidade com testes
    List<Caixa> findByIdIn(List<Long> ids);
    
    // Métodos de busca por dimensões
    List<Caixa> findByComprimentoBetweenAndAlturaBetweenAndLarguraBetween(
        double minComprimento, double maxComprimento,
        double minAltura, double maxAltura,
        double minLargura, double maxLargura
    );
    
    // Método de busca por peso
    List<Caixa> findByPesoBetween(double minPeso, double maxPeso);
}
