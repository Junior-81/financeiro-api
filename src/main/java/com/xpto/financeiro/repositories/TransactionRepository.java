package com.xpto.financeiro.repositories;

import com.xpto.financeiro.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);

    List<Transaction> findByAccountId(UUID accountId);

    List<Transaction> findAllByOrderByCreatedAtDesc();

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.client.id = :clientId")
    List<Transaction> findByClientId(@Param("clientId") UUID clientId);

    @Query("SELECT t FROM Transaction t JOIN t.account a WHERE a.client.id = :clientId AND DATE(t.createdAt) BETWEEN :startDate AND :endDate")
    List<Transaction> findByClientIdAndDateBetween(@Param("clientId") UUID clientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
