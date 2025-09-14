package com.xpto.financeiro.repositories.reports;

import com.xpto.financeiro.models.reports.ClientBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientTransactionInfoRepository extends JpaRepository<ClientBalance, String> {

    @Query(value = "{ call GetAllClientsTransactionInfo() }", nativeQuery = true)
    List<ClientBalance> findAllClientsTransactionInfo();

    @Query(value = "{ call GetClientTransactionInfo(:clientId) }", nativeQuery = true)
    ClientBalance findClientTransactionInfoById(@Param("clientId") String clientId);
}