package com.xpto.financeiro.models.reports;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;


@Entity
public class ClientTransactionInfo {
    @Id
    private String clientName;
    private Integer totalTransactions;
    private BigDecimal totalValueTransactions;

    
    public ClientTransactionInfo() {
    }

    public ClientTransactionInfo(String clientName, Integer totalTransactions, BigDecimal totalValueTransactions) {
        this.clientName = clientName;
        this.totalTransactions = totalTransactions;
        this.totalValueTransactions = totalValueTransactions;
    }

    
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getTotalValueTransactions() {
        return totalValueTransactions;
    }

    public void setTotalValueTransactions(BigDecimal totalValueTransactions) {
        this.totalValueTransactions = totalValueTransactions;
    }
}
