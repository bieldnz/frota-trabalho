package com.example.frota.caminhao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import com.example.frota.marca.Marca;
import com.example.frota.marca.MarcaService;

@Service
public class CaminhaoService {
	@Autowired
	private CaminhaoRepository caminhaoRepository;
	
	@Autowired
	private MarcaService marcaService;
	
	@Autowired
	private CaminhaoMapper caminhaoMapper;
	
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
	public void apagarPorId (Long id) {
        caminhaoRepository.apagarCaminhao(id);
	}
	
	public Optional<Caminhao> procurarPorId(Long id) {
	    return caminhaoRepository.findById(id);
	}

    public List<String> obterOperacoes() {
        return caminhaoRepository.operacoes();
    }
}
