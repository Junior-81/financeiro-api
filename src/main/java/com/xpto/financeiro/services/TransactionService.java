package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.CreateTransactionDTO;
import com.xpto.financeiro.dtos.ResponseTransactionDTO;
import com.xpto.financeiro.exceptions.ResourceNotFoundException;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Account;
import com.xpto.financeiro.models.Transaction;
import com.xpto.financeiro.repositories.AccountRepository;
import com.xpto.financeiro.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<ResponseTransactionDTO> findByAccountId(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
        return transactions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ResponseTransactionDTO> findAll() {
        List<Transaction> transactions = transactionRepository.findAllByOrderByCreatedAtDesc();
        return transactions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ResponseTransactionDTO create(UUID accountId, CreateTransactionDTO dto) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));

        // Validar saldo para débito
        if (dto.getOperationType() == Transaction.OperationType.D) {
            if (account.getBalance().compareTo(dto.getAmount()) < 0) {
                throw new ValidationException("Saldo insuficiente. Saldo atual: " + account.getBalance());
            }
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setOperationType(dto.getOperationType());
        transaction.setAccount(account);
        transaction.setCreatedAt(LocalDateTime.now());

        // O trigger irá atualizar o saldo automaticamente
        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToResponseDTO(savedTransaction);
    }

    public BigDecimal calculateAccountBalance(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        BigDecimal balance = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getOperationType() == Transaction.OperationType.C) {
                balance = balance.add(transaction.getAmount());
            } else {
                balance = balance.subtract(transaction.getAmount());
            }
        }

        return balance;
    }

    private ResponseTransactionDTO convertToResponseDTO(Transaction transaction) {
        return new ResponseTransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getOperationType(),
                transaction.getCreatedAt(),
                transaction.getAccount().getId());
    }
}
