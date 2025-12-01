package com.example.frota.caminhao;

import java.sql.Date;
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

    @Procedure(procedureName = "listar_logs_caminhao")
        List<String> operacoes(@Param("p_caminhao_id") int cod_caminhao, @Param("p_data_inicio") Date data_inicio, @Param("p_data_fim") Date data_fim);
    
}
