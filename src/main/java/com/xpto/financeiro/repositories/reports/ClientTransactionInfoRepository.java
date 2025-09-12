package com.xpto.financeiro.repositories.reports;

import com.xpto.financeiro.models.reports.ClientTransactionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientTransactionInfoRepository extends JpaRepository<ClientTransactionInfo, UUID> {
    
    @Query(value = "{ call GetClientTransactionInfo(:clientId) }", nativeQuery = true)
    List<ClientTransactionInfo> findClientTransactionInfoById(@Param("clientId") UUID clientId);
}
