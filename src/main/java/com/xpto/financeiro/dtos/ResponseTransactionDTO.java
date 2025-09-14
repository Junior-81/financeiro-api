package com.xpto.financeiro.dtos;

import com.xpto.financeiro.models.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class ResponseTransactionDTO {
    private UUID id;
    private BigDecimal amount;
    private Transaction.OperationType operationType;
    private LocalDateTime createdAt;
    private UUID accountId;

    public ResponseTransactionDTO() {}

    public ResponseTransactionDTO(UUID id, BigDecimal amount, Transaction.OperationType operationType, 
                                LocalDateTime createdAt, UUID accountId) {
        this.id = id;
        this.amount = amount;
        this.operationType = operationType;
        this.createdAt = createdAt;
        this.accountId = accountId;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Transaction.OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(Transaction.OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }
}
