package com.xpto.financeiro.controllers;

import java.util.UUID;

import com.xpto.financeiro.dtos.CreateTransactionDTO;
import com.xpto.financeiro.dtos.ResponseTransactionDTO;
import com.xpto.financeiro.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/account/{accountId}")
    public ResponseEntity<ResponseTransactionDTO> createTransaction(
            @PathVariable UUID accountId,
            @Valid @RequestBody CreateTransactionDTO dto) {
        ResponseTransactionDTO transaction = transactionService.create(accountId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<ResponseTransactionDTO>> getAllTransactions() {
        List<ResponseTransactionDTO> transactions = transactionService.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ResponseTransactionDTO>> getTransactionsByAccount(@PathVariable UUID accountId) {
        List<ResponseTransactionDTO> transactions = transactionService.findByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
