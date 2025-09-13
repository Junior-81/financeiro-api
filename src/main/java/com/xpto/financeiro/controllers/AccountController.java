package com.xpto.financeiro.controllers;

import com.xpto.financeiro.dtos.CreateAccountDTO;
import com.xpto.financeiro.models.Account;
import com.xpto.financeiro.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.findAll();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountDTO dto) {
        Account account = accountService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }
}
