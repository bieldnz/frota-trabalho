package com.example.frota.transporte;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TransporteRepository extends JpaRepository<Transporte, Long> {

    @Query("SELECT t FROM Transporte t JOIN FETCH t.caixa LEFT JOIN FETCH t.cliente LEFT JOIN FETCH t.transportadora WHERE t.id = :id")
    Optional<Transporte> findByIdWithCaixa(@Param("id") Long id);

    @Query("SELECT t FROM Transporte t JOIN FETCH t.caixa LEFT JOIN FETCH t.cliente LEFT JOIN FETCH t.transportadora")
    List<Transporte> findAllWithCaixa();
    
  
    @Query("SELECT t FROM Transporte t JOIN FETCH t.caixa c LEFT JOIN FETCH t.cliente LEFT JOIN FETCH t.transportadora WHERE c.id = :caixaId")
    List<Transporte> findAllByCaixaIdWithCaixa(@Param("caixaId") Long caixaId);

    @Query("SELECT t FROM Transporte t JOIN FETCH t.caixa LEFT JOIN FETCH t.cliente LEFT JOIN FETCH t.transportadora WHERE t.id IN :ids")
    List<Transporte> findAllByIdsWithCaixa(@Param("ids") List<Long> ids);
}