package com.example.frota.motorista;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    
    // Buscar motoristas ativos
    List<Motorista> findByAtivoTrue();
    
    // Buscar motoristas disponíveis
    List<Motorista> findByDisponivelTrue();
    
    // Buscar motoristas ativos e disponíveis
    List<Motorista> findByAtivoTrueAndDisponivelTrue();
    
    // Buscar por CPF
    Optional<Motorista> findByCpf(String cpf);
    
    // Buscar por CNH
    Optional<Motorista> findByCnh(String cnh);
    
    // Verificar se CPF já existe
    boolean existsByCpf(String cpf);
    
    // Verificar se CNH já existe
    boolean existsByCnh(String cnh);
    
    // Verificar se CPF já existe para outro ID
    boolean existsByCpfAndIdNot(String cpf, Long id);
    
    // Verificar se CNH já existe para outro ID
    boolean existsByCnhAndIdNot(String cnh, Long id);
    
    // Buscar motoristas por nome
    @Query("SELECT m FROM Motorista m WHERE LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Motorista> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    // Paginação para motoristas ativos
    Page<Motorista> findByAtivoTrue(Pageable pageable);
    
    // Contar motoristas disponíveis
    @Query("SELECT COUNT(m) FROM Motorista m WHERE m.ativo = true AND m.disponivel = true")
    Long countMotoristaDisponiveis();
}