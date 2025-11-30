package com.example.frota.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    /**
     * Busca clientes ativos
     */
    Page<Cliente> findByAtivoTrue(Pageable pageable);
    
    /**
     * Busca clientes inativos
     */
    Page<Cliente> findByAtivoFalse(Pageable pageable);
    
    /**
     * Busca por email (ignorando maiúscula/minúscula)
     */
    Optional<Cliente> findByEmailIgnoreCase(String email);
    
    /**
     * Verifica se existe cliente com o email (ignorando maiúscula/minúscula)
     */
    boolean existsByEmailIgnoreCase(String email);
    
    /**
     * Verifica se existe cliente com o email, excluindo um ID específico
     */
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE LOWER(c.email) = LOWER(:email) AND c.id != :id")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    /**
     * Busca clientes por nome (contendo o texto)
     */
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND c.ativo = true")
    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoTrue(@Param("nome") String nome);
    
    /**
     * Busca clientes por telefone
     */
    Optional<Cliente> findByTelefone(String telefone);
    
    /**
     * Busca todos os clientes ativos ordenados por nome
     */
    @Query("SELECT c FROM Cliente c WHERE c.ativo = true ORDER BY c.nome ASC")
    List<Cliente> findAllActiveOrderByNome();
    
    /**
     * Conta total de clientes ativos
     */
    long countByAtivoTrue();
    
    /**
     * Conta total de clientes inativos
     */
    long countByAtivoFalse();
}
