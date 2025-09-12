package com.xpto.financeiro.models.reports;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Model para mapear relaotorio de movimentações do cliente
public class ClientBalanceDetail {
    private String name;
    private Timestamp createdAt;
    private String address;
    private Integer creditTransactions;
    private Integer debitTransactions;
    private Integer totalTransactions;
    private BigDecimal valueTransactions;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;

    // Construtores
    public ClientBalanceDetail() {}

    public ClientBalanceDetail(String name, Timestamp createdAt, String address, 
                             Integer creditTransactions, Integer debitTransactions, 
                             Integer totalTransactions, BigDecimal valueTransactions, 
                             BigDecimal initialBalance, BigDecimal currentBalance) {
        this.name = name;
        this.createdAt = createdAt;
        this.address = address;
        this.creditTransactions = creditTransactions;
        this.debitTransactions = debitTransactions;
        this.totalTransactions = totalTransactions;
        this.valueTransactions = valueTransactions;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCreditTransactions() {
        return creditTransactions;
    }

    public void setCreditTransactions(Integer creditTransactions) {
        this.creditTransactions = creditTransactions;
    }

    public Integer getDebitTransactions() {
        return debitTransactions;
    }

    public void setDebitTransactions(Integer debitTransactions) {
        this.debitTransactions = debitTransactions;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getValueTransactions() {
        return valueTransactions;
    }

    public void setValueTransactions(BigDecimal valueTransactions) {
        this.valueTransactions = valueTransactions;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}
