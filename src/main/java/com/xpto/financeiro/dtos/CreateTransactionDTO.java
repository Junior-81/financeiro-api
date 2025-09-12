package com.xpto.financeiro.dtos;

import com.xpto.financeiro.models.Transaction;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateTransactionDTO {
    @NotNull
    private BigDecimal amount;
    
    @NotNull
    private Transaction.OperationType operationType;

    // Construtores
    public CreateTransactionDTO() {}

    public CreateTransactionDTO(BigDecimal amount, Transaction.OperationType operationType) {
        this.amount = amount;
        this.operationType = operationType;
    }

    // Getters e Setters
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
}
