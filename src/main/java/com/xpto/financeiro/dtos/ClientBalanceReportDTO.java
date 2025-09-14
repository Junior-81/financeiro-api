package com.xpto.financeiro.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class ClientBalanceReportDTO {
    private UUID clientId;
    private String clientName;
    private LocalDate clientSince;
    private String address;
    private int creditMovements;
    private int debitMovements;
    private int totalMovements;
    private BigDecimal feePaid;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private LocalDate reportDate;
    private String period;


    public ClientBalanceReportDTO() {
    }

    public ClientBalanceReportDTO(UUID clientId, String clientName, LocalDate clientSince, String address,
            int creditMovements, int debitMovements, int totalMovements,
            BigDecimal feePaid, BigDecimal initialBalance, BigDecimal currentBalance) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientSince = clientSince;
        this.address = address;
        this.creditMovements = creditMovements;
        this.debitMovements = debitMovements;
        this.totalMovements = totalMovements;
        this.feePaid = feePaid;
        this.initialBalance = initialBalance;
        this.currentBalance = currentBalance;
        this.reportDate = LocalDate.now();
    }

   
    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getClientSince() {
        return clientSince;
    }

    public void setClientSince(LocalDate clientSince) {
        this.clientSince = clientSince;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCreditMovements() {
        return creditMovements;
    }

    public void setCreditMovements(int creditMovements) {
        this.creditMovements = creditMovements;
    }

    public int getDebitMovements() {
        return debitMovements;
    }

    public void setDebitMovements(int debitMovements) {
        this.debitMovements = debitMovements;
    }

    public int getTotalMovements() {
        return totalMovements;
    }

    public void setTotalMovements(int totalMovements) {
        this.totalMovements = totalMovements;
    }

    public BigDecimal getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(BigDecimal feePaid) {
        this.feePaid = feePaid;
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}