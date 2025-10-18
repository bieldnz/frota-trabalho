package com.example.frota.caixa;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Repository

public interface CaixaRepository extends JpaRepository<Caixa, Long> {
    Optional<Caixa> findById(Long id);

}
