package com.xpto.financeiro.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class CreateAccountDTO {
    @NotNull(message = "Client ID is required")
    private UUID clientId;
    
    private String accountNumber;
    
    @Positive(message = "Initial balance must be positive")
    private BigDecimal initialBalance = BigDecimal.ZERO;
    
    
    public UUID getClientId() {
        return clientId;
    }
    
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
    
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}