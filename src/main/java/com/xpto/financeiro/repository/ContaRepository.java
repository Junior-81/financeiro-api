package com.xpto.financeiro.repository;

import com.xpto.financeiro.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    // Buscar contas ativas
    List<Conta> findByAtivoTrue();
    
    // Buscar contas por cliente
    List<Conta> findByClienteIdAndAtivoTrue(Long clienteId);
    
    // Buscar conta por número e agência
    @Query("SELECT c FROM Conta c WHERE c.numero = :numero AND c.agencia = :agencia AND c.ativo = true")
    Optional<Conta> findByNumeroAndAgencia(@Param("numero") String numero, @Param("agencia") String agencia);
    
    // Buscar contas por instituição financeira
    List<Conta> findByInstituicaoFinanceiraAndAtivoTrue(String instituicaoFinanceira);
    
    // Contar contas por cliente
    @Query("SELECT COUNT(c) FROM Conta c WHERE c.cliente.id = :clienteId AND c.ativo = true")
    Long countByClienteId(@Param("clienteId") Long clienteId);
    
    // Buscar contas com movimentações
    @Query("SELECT DISTINCT c FROM Conta c JOIN c.movimentacoes m WHERE c.ativo = true")
    List<Conta> findContasComMovimentacoes();
}
