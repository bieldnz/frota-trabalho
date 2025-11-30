package com.example.frota.transportadora;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {
    
    /**
     * Busca transportadoras ativas
     */
    Page<Transportadora> findByAtivoTrue(Pageable pageable);
    
    /**
     * Busca transportadoras inativas
     */
    Page<Transportadora> findByAtivoFalse(Pageable pageable);
    
    /**
     * Busca por CNPJ
     */
    Optional<Transportadora> findByCnpj(String cnpj);
    
    /**
     * Busca por email (ignorando maiúscula/minúscula)
     */
    Optional<Transportadora> findByEmailIgnoreCase(String email);
    
    /**
     * Verifica se existe transportadora com o CNPJ
     */
    boolean existsByCnpj(String cnpj);
    
    /**
     * Verifica se existe transportadora com o email (ignorando maiúscula/minúscula)
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Verifica se existe transportadora com o CNPJ, excluindo um ID específico
     */
    @Query("SELECT COUNT(t) > 0 FROM Transportadora t WHERE t.cnpj = :cnpj AND t.id != :id")
    boolean existsByCnpjAndIdNot(@Param("cnpj") String cnpj, @Param("id") Long id);
    
    /**
     * Verifica se existe transportadora com o email, excluindo um ID específico
     */
    @Query("SELECT COUNT(t) > 0 FROM Transportadora t WHERE LOWER(t.email) = LOWER(:email) AND t.id != :id")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    /**
     * Busca transportadoras por nome (contendo o texto)
     */
    @Query("SELECT t FROM Transportadora t WHERE LOWER(t.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND t.ativo = true")
    List<Transportadora> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);
    
    /**
     * Busca transportadoras por CNPJ (contendo o texto)
     */
    @Query("SELECT t FROM Transportadora t WHERE t.cnpj LIKE CONCAT('%', :cnpj, '%') AND t.ativo = true")
    List<Transportadora> findByCnpjContainingAndAtivoTrue(@Param("cnpj") String cnpj);
    
    /**
     * Busca por telefone
     */
    Optional<Transportadora> findByTelefone(String telefone);
    
    /**
     * Busca todas as transportadoras ativas ordenadas por nome
     */
    @Query("SELECT t FROM Transportadora t WHERE t.ativo = true ORDER BY t.nome ASC")
    List<Transportadora> findAllActiveOrderByNome();
    
    /**
     * Busca transportadoras com avaliação maior ou igual
     */
    @Query("SELECT t FROM Transportadora t WHERE t.ativo = true AND t.avaliacao >= :avaliacao ORDER BY t.avaliacao DESC")
    List<Transportadora> findByAvaliacaoGreaterThanEqualAndAtivoTrueOrderByAvaliacaoDesc(@Param("avaliacao") Double avaliacao);
    
    /**
     * Busca top transportadoras por avaliação
     */
    @Query("SELECT t FROM Transportadora t WHERE t.ativo = true ORDER BY t.avaliacao DESC")
    List<Transportadora> findTopTransportadorasByAvaliacao(Pageable pageable);
    
    /**
     * Conta total de transportadoras ativas
     */
    long countByAtivoTrue();
    
    /**
     * Conta total de transportadoras inativas
     */
    long countByAtivoFalse();
    
    /**
     * Calcula média de avaliações das transportadoras ativas
     */
    @Query("SELECT AVG(t.avaliacao) FROM Transportadora t WHERE t.ativo = true AND t.avaliacao IS NOT NULL")
    Double findAverageAvaliacaoByAtivoTrue();
}