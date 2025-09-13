package com.xpto.financeiro.repositories.reports;

import com.xpto.financeiro.models.reports.ClientBalanceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientBalanceDetailRepository extends JpaRepository<ClientBalanceDetail, String> {

    @Query(value = "{ call GetClientBalanceDetail(:clientId) }", nativeQuery = true)
    List<ClientBalanceDetail> findClientBalanceDetailById(@Param("clientId") String clientId);
}
