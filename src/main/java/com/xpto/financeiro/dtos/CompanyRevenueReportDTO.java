package com.xpto.financeiro.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CompanyRevenueReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ClientRevenueDTO> clientRevenues;
    private BigDecimal totalRevenue;
    private LocalDate reportDate;

    
    public CompanyRevenueReportDTO() {
    }

    public CompanyRevenueReportDTO(LocalDate startDate, LocalDate endDate,
            List<ClientRevenueDTO> clientRevenues, BigDecimal totalRevenue) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientRevenues = clientRevenues;
        this.totalRevenue = totalRevenue;
        this.reportDate = LocalDate.now();
    }

    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<ClientRevenueDTO> getClientRevenues() {
        return clientRevenues;
    }

    public void setClientRevenues(List<ClientRevenueDTO> clientRevenues) {
        this.clientRevenues = clientRevenues;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }
    public static class ClientRevenueDTO {
        private UUID clientId;
        private String clientName;
        private int totalMovements;
        private BigDecimal revenue;

        public ClientRevenueDTO() {
        }

        public ClientRevenueDTO(UUID clientId, String clientName, int totalMovements, BigDecimal revenue) {
            this.clientId = clientId;
            this.clientName = clientName;
            this.totalMovements = totalMovements;
            this.revenue = revenue;
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

        public int getTotalMovements() {
            return totalMovements;
        }

        public void setTotalMovements(int totalMovements) {
            this.totalMovements = totalMovements;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }
}