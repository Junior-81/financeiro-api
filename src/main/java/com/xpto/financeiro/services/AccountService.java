package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.CreateAccountDTO;
import com.xpto.financeiro.exceptions.ResourceNotFoundException;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Account;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.repositories.AccountRepository;
import com.xpto.financeiro.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ClientRepository clientRepository;

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com ID: " + id));
    }

    public List<Account> findByClientId(UUID clientId) {
        return accountRepository.findByClientId(clientId);
    }

    public Account create(CreateAccountDTO dto) {
        // Verificar se cliente existe
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        // Verificar se número da conta já existe
        if (accountRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new ValidationException("Número da conta já existe: " + dto.getAccountNumber());
        }

        Account account = new Account();
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getInitialBalance());
        account.setClient(client);
        account.setCreatedAt(LocalDateTime.now());
        
        return accountRepository.save(account);
    }

    public Account updateBalance(UUID accountId, BigDecimal newBalance) {
        Account account = findById(accountId);
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void delete(UUID id) {
        Account account = findById(id);
        // Verificar se existe movimentações - implementar lógica de exclusão lógica se necessário
        accountRepository.delete(account);
    }
}
