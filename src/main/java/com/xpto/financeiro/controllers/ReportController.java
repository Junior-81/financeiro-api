package com.xpto.financeiro.controllers;

import com.xpto.financeiro.dtos.ClientBalanceReportDTO;
import com.xpto.financeiro.dtos.CompanyRevenueReportDTO;
import com.xpto.financeiro.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "API para relatórios de saldo e receita")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/client/{clientId}/balance")
    @Operation(summary = "Relatório de saldo do cliente", description = "Retorna o relatório de saldo de um cliente específico")
    public ResponseEntity<ClientBalanceReportDTO> getClientBalance(
            @Parameter(description = "ID do cliente") @PathVariable String clientId) {
        ClientBalanceReportDTO report = reportService.getClientBalanceReport(clientId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/client/{clientId}/balance/period")
    @Operation(summary = "Relatório de saldo do cliente por período", description = "Retorna o relatório de saldo de um cliente em um período específico")
    public ResponseEntity<ClientBalanceReportDTO> getClientBalanceByPeriod(
            @Parameter(description = "ID do cliente") @PathVariable String clientId,
            @Parameter(description = "Data inicial (yyyy-mm-dd)") @RequestParam String startDate,
            @Parameter(description = "Data final (yyyy-mm-dd)") @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        ClientBalanceReportDTO report = reportService.getClientBalanceReportByPeriod(clientId, start, end);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/clients/balance")
    @Operation(summary = "Relatório de saldo de todos os clientes", description = "Retorna o relatório de saldo de todos os clientes")
    public ResponseEntity<List<ClientBalanceReportDTO>> getAllClientsBalance() {
        List<ClientBalanceReportDTO> reports = reportService.getAllClientsBalanceReport();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/company/revenue")
    @Operation(summary = "Relatório de receita da empresa", description = "Retorna o relatório de receita da empresa XPTO por período")
    public ResponseEntity<CompanyRevenueReportDTO> getCompanyRevenue(
            @Parameter(description = "Data inicial (yyyy-mm-dd)") @RequestParam String startDate,
            @Parameter(description = "Data final (yyyy-mm-dd)") @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        CompanyRevenueReportDTO report = reportService.getCompanyRevenueReport(start, end);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/client/{clientId}/fee")
    @Operation(summary = "Calcula tarifa do cliente usando PL/SQL", description = "Calcula a tarifa do cliente usando função PL/SQL no Oracle")
    public ResponseEntity<Double> getClientFeeFromPLSQL(
            @Parameter(description = "ID do cliente") @PathVariable String clientId) {
        Double fee = reportService.getClientFeeFromPLSQL(clientId);
        return ResponseEntity.ok(fee);
    }
}