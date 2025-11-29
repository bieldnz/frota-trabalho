package com.example.frota.manutencao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    // buscar a última manutenção de um tipo específico para um caminhão
    @Query("SELECT m FROM Manutencao m WHERE m.caminhao.id = :caminhaoId AND m.tipoServico = :tipoServico ORDER BY m.kmRealizacao DESC LIMIT 1")
    Optional<Manutencao> findUltimaManutencaoPorCaminhaoETipo(
        @Param("caminhaoId") Long caminhaoId, 
        @Param("tipoServico") TipoManutencao tipoServico
    );

    @Query("SELECT m FROM Manutencao m JOIN FETCH m.caminhao WHERE m.id = :id")
    Optional<Manutencao> findByIdWithCaminhao(@Param("id") Long id);

    @Query("SELECT m FROM Manutencao m JOIN FETCH m.caminhao")
    List<Manutencao> findAllWithCaminhao();

}