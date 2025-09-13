package com.xpto.financeiro.models.reports;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

// Model para mapear relatorio de saldo dos clientes
@Entity
public class ClientBalance {
    @Id
    private String clientName;
    private Timestamp createAt;
    private BigDecimal currentBalance;

    // Construtores
    public ClientBalance() {}

    public ClientBalance(String clientName, Timestamp createAt, BigDecimal currentBalance) {
        this.clientName = clientName;
        this.createAt = createAt;
        this.currentBalance = currentBalance;
    }

    // Getters e Setters
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}
