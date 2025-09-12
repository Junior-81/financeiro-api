package com.xpto.financeiro.repository;

import com.xpto.financeiro.entity.Movimentacao;
import com.xpto.financeiro.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    
    // Buscar movimentações por conta
    List<Movimentacao> findByContaIdOrderByDataMovimentacaoDesc(Long contaId);
    
    // Buscar movimentações por período
    @Query("SELECT m FROM Movimentacao m WHERE m.dataMovimentacao BETWEEN :dataInicio AND :dataFim ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByPeriodo(@Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
    
    // Buscar movimentações por cliente e período
    @Query("SELECT m FROM Movimentacao m WHERE m.conta.cliente.id = :clienteId AND m.dataMovimentacao BETWEEN :dataInicio AND :dataFim ORDER BY m.dataMovimentacao DESC")
    List<Movimentacao> findByClienteAndPeriodo(@Param("clienteId") Long clienteId, @Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
    
    // Contar movimentações por tipo e cliente
    @Query("SELECT COUNT(m) FROM Movimentacao m WHERE m.conta.cliente.id = :clienteId AND m.tipo = :tipo")
    Long countByClienteAndTipo(@Param("clienteId") Long clienteId, @Param("tipo") TipoMovimentacao tipo);
    
    // Somar valores por tipo e cliente
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimentacao m WHERE m.conta.cliente.id = :clienteId AND m.tipo = :tipo")
    BigDecimal sumValorByClienteAndTipo(@Param("clienteId") Long clienteId, @Param("tipo") TipoMovimentacao tipo);
    
    // Contar movimentações por cliente em período específico (para cálculo de tarifas)
    @Query("SELECT COUNT(m) FROM Movimentacao m WHERE m.conta.cliente.id = :clienteId AND m.dataMovimentacao BETWEEN :dataInicio AND :dataFim")
    Long countMovimentacoesPorPeriodo(@Param("clienteId") Long clienteId, @Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);
}