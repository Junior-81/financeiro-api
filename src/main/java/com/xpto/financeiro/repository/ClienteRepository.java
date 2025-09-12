package com.xpto.financeiro.repository;

import com.xpto.financeiro.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar clientes ativos
    List<Cliente> findByAtivoTrue();
    
    // Buscar cliente por CPF (PF)
    @Query("SELECT pf FROM PessoaFisica pf WHERE pf.cpf = :cpf AND pf.ativo = true")
    Optional<Cliente> findByCpf(@Param("cpf") String cpf);
    
    // Buscar cliente por CNPJ (PJ)
    @Query("SELECT pj FROM PessoaJuridica pj WHERE pj.cnpj = :cnpj AND pj.ativo = true")
    Optional<Cliente> findByCnpj(@Param("cnpj") String cnpj);
    
    // Buscar clientes por nome
    @Query("SELECT c FROM Cliente c WHERE UPPER(c.nome) LIKE UPPER(CONCAT('%', :nome, '%')) AND c.ativo = true")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    // Contar total de clientes ativos
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.ativo = true")
    Long countClientesAtivos();
    
    // Buscar clientes com saldo maior que valor
    @Query("SELECT c FROM Cliente c WHERE c.saldoInicial > :valor AND c.ativo = true")
    List<Cliente> findClientesComSaldoMaiorQue(@Param("valor") Double valor);
}