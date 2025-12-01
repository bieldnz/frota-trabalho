package com.example.frota.caminhao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional
public interface CaminhaoRepository extends JpaRepository<Caminhao, Long>{
     @Procedure(procedureName = "apagar_caminhao")
        void apagarCaminhao(@Param("caminhao_id") Long caminhaoId);

    @Procedure(procedureName = "operacoes")
        List<String> operacoes();
    
}
