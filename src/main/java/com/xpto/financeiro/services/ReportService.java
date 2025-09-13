package com.xpto.financeiro.services;

import java.util.UUID;

import com.xpto.financeiro.dtos.ClientBalanceReportDTO;
import com.xpto.financeiro.dtos.CompanyRevenueReportDTO;
import com.xpto.financeiro.dtos.CompanyRevenueReportDTO.ClientRevenueDTO;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.models.Transaction;
import com.xpto.financeiro.repositories.ClientRepository;
import com.xpto.financeiro.repositories.TransactionRepository;
import com.xpto.financeiro.repositories.reports.ClientBalancePLSQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReportService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientBalancePLSQLRepository clientBalancePLSQLRepository;

    public ClientBalanceReportDTO getClientBalanceReport(String clientId) {
        UUID clientUUID = UUID.fromString(clientId);
        Optional<Client> clientOpt = clientRepository.findById(clientUUID);
        if (!clientOpt.isPresent()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        Client client = clientOpt.get();
        List<Transaction> transactions = transactionRepository.findByClientId(clientUUID);

        return buildClientBalanceReport(client, transactions, null, null);
    }

    public ClientBalanceReportDTO getClientBalanceReportByPeriod(String clientId, LocalDate startDate,
            LocalDate endDate) {
        UUID clientUUID = UUID.fromString(clientId);
        Optional<Client> clientOpt = clientRepository.findById(clientUUID);
        if (!clientOpt.isPresent()) {
            throw new RuntimeException("Cliente não encontrado");
        }

        Client client = clientOpt.get();
        List<Transaction> transactions = transactionRepository.findByClientIdAndDateBetween(clientUUID, startDate,
                endDate);

        ClientBalanceReportDTO report = buildClientBalanceReport(client, transactions, startDate, endDate);
        report.setPeriod(startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                " a " + endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return report;
    }

    public List<ClientBalanceReportDTO> getAllClientsBalanceReport() {
        List<Client> clients = clientRepository.findAll();
        List<ClientBalanceReportDTO> reports = new ArrayList<>();

        for (Client client : clients) {
            List<Transaction> transactions = transactionRepository.findByClientId(client.getId());
            ClientBalanceReportDTO report = buildClientBalanceReport(client, transactions, null, null);
            reports.add(report);
        }

        return reports;
    }

    public CompanyRevenueReportDTO getCompanyRevenueReport(LocalDate startDate, LocalDate endDate) {
        List<Client> clients = clientRepository.findAll();
        List<ClientRevenueDTO> clientRevenues = new ArrayList<>();
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (Client client : clients) {
            List<Transaction> transactions = transactionRepository.findByClientIdAndDateBetween(
                    client.getId(), startDate, endDate);

            int totalMovements = transactions.size();
            BigDecimal clientRevenue = calculateClientFee(totalMovements);

            ClientRevenueDTO clientRevenueDTO = new ClientRevenueDTO(
                    client.getId(), client.getName(), totalMovements, clientRevenue);
            clientRevenues.add(clientRevenueDTO);

            totalRevenue = totalRevenue.add(clientRevenue);
        }

        return new CompanyRevenueReportDTO(startDate, endDate, clientRevenues, totalRevenue);
    }

    public Double getClientFeeFromPLSQL(String clientId) {
        UUID clientUUID = UUID.fromString(clientId);
        return clientBalancePLSQLRepository.getClientFee(clientUUID);
    }

    private ClientBalanceReportDTO buildClientBalanceReport(Client client, List<Transaction> transactions,
            LocalDate startDate, LocalDate endDate) {
        int creditMovements = 0;
        int debitMovements = 0;
        BigDecimal currentBalance = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                creditMovements++;
            } else {
                debitMovements++;
            }
            currentBalance = currentBalance.add(transaction.getAmount());
        }

        int totalMovements = creditMovements + debitMovements;
        BigDecimal feePaid = calculateClientFee(totalMovements);

        String address = formatClientAddress(client);

        return new ClientBalanceReportDTO(
                client.getId(),
                client.getName(),
                client.getCreatedAt().toLocalDate(),
                address,
                creditMovements,
                debitMovements,
                totalMovements,
                feePaid,
                BigDecimal.ZERO, // Initial balance - pode ser calculado baseado nas contas
                currentBalance);
    }

    private BigDecimal calculateClientFee(int totalMovements) {
        BigDecimal fee = BigDecimal.ZERO;

        if (totalMovements <= 10) {
            fee = BigDecimal.valueOf(totalMovements * 1.00); // R$ 1,00 por movimentação
        } else if (totalMovements <= 20) {
            fee = BigDecimal.valueOf(10 * 1.00); // Primeiras 10 movimentações
            fee = fee.add(BigDecimal.valueOf((totalMovements - 10) * 0.75)); // R$ 0,75 para as próximas
        } else {
            fee = BigDecimal.valueOf(10 * 1.00); // Primeiras 10
            fee = fee.add(BigDecimal.valueOf(10 * 0.75)); // Próximas 10
            fee = fee.add(BigDecimal.valueOf((totalMovements - 20) * 0.50)); // R$ 0,50 para as demais
        }

        return fee;
    }

    private String formatClientAddress(Client client) {
        if (client.getAddress() != null) {
            return String.format("%s, %d, %s, %s, %s, %s - %s",
                    client.getAddress().getStreet(),
                    client.getAddress().getAddressNumber(),
                    client.getAddress().getNeighborhood(),
                    client.getAddress().getCity(),
                    client.getAddress().getState(),
                    client.getAddress().getZipCode(),
                    "Brasil");
        }
        return "Endereço não informado";
    }
}