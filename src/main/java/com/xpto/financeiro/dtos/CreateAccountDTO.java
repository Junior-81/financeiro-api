package com.xpto.financeiro.dtos;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateAccountDTO {
    @NotNull
    private String accountNumber;
    
    @NotNull
    private BigDecimal initialBalance;

    // Construtores
    public CreateAccountDTO() {}

    public CreateAccountDTO(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
    }

    // Getters e Setters
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
