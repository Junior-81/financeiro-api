package com.xpto.financeiro.repositories.reports;

import com.xpto.financeiro.models.reports.ClientBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientBalanceRepository extends JpaRepository<ClientBalance, UUID> {
    
    @Query(value = "{ call GetClientBalance() }", nativeQuery = true)
    List<ClientBalance> findAllClientBalances();
    
    @Query(value = "{ call GetClientBalanceById(:clientId) }", nativeQuery = true)
    Optional<ClientBalance> findClientBalanceById(@Param("clientId") UUID clientId);
}
