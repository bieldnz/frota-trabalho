package com.example.frota.caminhao;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import com.example.frota.marca.Marca;
import com.example.frota.marca.MarcaService;

@Service
public class CaminhaoService {
	
	private static final Logger log = Logger.getLogger(CaminhaoService.class.getName());
	
	@Autowired
	private CaminhaoRepository caminhaoRepository;
	
	@Autowired
	private MarcaService marcaService;
	
	@Autowired
	private CaminhaoMapper caminhaoMapper;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Caminhao salvarOuAtualizar(AtualizacaoCaminhao dto) {
             Marca marca = marcaService.procurarPorId(dto.marcaId())
            .orElseThrow(() -> new EntityNotFoundException("Marca não encontrada com ID: " + dto.marcaId()));
        if (dto.id() != null) {
            Caminhao existente = caminhaoRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Caminhão não encontrado com ID: " + dto.id()));
            caminhaoMapper.updateEntityFromDto(dto, existente);
            existente.setMarca(marca); 
            return caminhaoRepository.save(existente);
        } else {
            Caminhao novoCaminhao = caminhaoMapper.toEntityFromAtualizacao(dto);
            novoCaminhao.setMarca(marca);
            
            return caminhaoRepository.save(novoCaminhao);
        }
    }

    public Caminhao salvarOuAtualizar(Caminhao caminhao) {
        return caminhaoRepository.save(caminhao);
    }
	
	public List<Caminhao> procurarTodos(){
		return caminhaoRepository.findAll(Sort.by("modelo").ascending());
	}
	
	public void apagarPorId(Long id) {
        try {
            log.info("SERVICE: Apagando caminhão com ID: " + id);
            
            // Chama o procedimento usando JdbcTemplate
            String sql = "CALL apagar_caminhao(?)";
            jdbcTemplate.execute(sql, (java.sql.PreparedStatement ps) -> {
                ps.setLong(1, id);
                ps.execute();
                return null;
            });
            
            log.info("SERVICE: Caminhão apagado com sucesso");
        } catch (Exception e) {
            log.severe("ERRO ao apagar caminhão: " + e.getMessage());
            throw new RuntimeException("Erro ao apagar caminhão com ID " + id, e);
        }
    }
	
	public Optional<Caminhao> procurarPorId(Long id) {
	    return caminhaoRepository.findById(id);
	}

    public List<LogsCaminha> obterOperacoes(Integer cod_caminhao, Date data_inicio, Date data_fim) {
        try {
            log.info("SERVICE: Executando query com parâmetros: " + cod_caminhao + ", " + data_inicio + ", " + data_fim);
            
            // Primeiro, vamos testar com uma query SQL simples
            String sql = "SELECT cod_log, cod_caminhao, alteracao, data_alt FROM Tabela_Log_Caminhao WHERE (? IS NULL OR cod_caminhao = ?) ORDER BY data_alt DESC";
            
            List<LogsCaminha> resultado = jdbcTemplate.query(sql, 
                new Object[]{cod_caminhao, cod_caminhao},
                (rs, rowNum) -> {
                    LogsCaminha logItem = new LogsCaminha(
                        rs.getInt("cod_log"),
                        rs.getInt("cod_caminhao"), 
                        rs.getString("alteracao"),
                        rs.getTimestamp("data_alt")
                    );
                    log.info("SERVICE: Log encontrado: " + logItem.getCod_log() + ", " + logItem.getCod_caminhao() + ", " + logItem.getAlteracao());
                    return logItem;
                });
            
            log.info("SERVICE: Total de logs encontrados: " + resultado.size());
            return resultado;
        } catch (Exception e) {
            log.severe("Erro ao executar query: " + e.getMessage());
            e.printStackTrace();
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }
}
