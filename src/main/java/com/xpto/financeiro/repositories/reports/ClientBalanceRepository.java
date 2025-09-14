package com.xpto.financeiro.repositories.reports;

import com.xpto.financeiro.models.reports.ClientTransactionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientBalanceRepository extends JpaRepository<ClientTransactionInfo, String> {

    @Query(value = "{ call GetClientTransactionInfo(:clientId, :startDate, :endDate) }", nativeQuery = true)
    List<ClientTransactionInfo> findClientTransactionInfo(
            @Param("clientId") String clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}