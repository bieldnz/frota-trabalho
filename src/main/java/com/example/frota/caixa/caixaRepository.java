package com.example.frota.caixa;

import java.lang.foreign.Linker.Option;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Repository

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    // Métodos de consulta personalizados, se necessário
    List<Caixa> findByMaterial(String material);
    List<Caixa> findByDisponivel(boolean disponivel);
    List<Caixa> findByCapacidadeKgMaiorQue(double capacidadeKg);
    List<Caixa> findByAlturaMenorQue(double altura);
    List<Caixa> findByLarguraMenorQue(double largura);
    List<Caixa> findByProfundidadeMenorQue(double profundidade);
    Optional<Caixa> findById(Long id);


}
