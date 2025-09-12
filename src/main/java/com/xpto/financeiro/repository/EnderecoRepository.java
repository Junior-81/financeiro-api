package com.xpto.financeiro.repository;

import com.xpto.financeiro.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
    // Buscar endereços ativos
    List<Endereco> findByAtivoTrue();
    
    // Buscar endereços por cliente
    List<Endereco> findByClienteIdAndAtivoTrue(Long clienteId);
    
    // Buscar endereços por CEP
    List<Endereco> findByCepAndAtivoTrue(String cep);
    
    // Buscar endereços por cidade e UF
    @Query("SELECT e FROM Endereco e WHERE UPPER(e.cidade) = UPPER(:cidade) AND UPPER(e.uf) = UPPER(:uf) AND e.ativo = true")
    List<Endereco> findByCidadeAndUf(@Param("cidade") String cidade, @Param("uf") String uf);
}
