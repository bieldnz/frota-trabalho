package com.example.frota.viagem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {
    
    List<Viagem> findByCaminhaoId(Long caminhaoId);
}