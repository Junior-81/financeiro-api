package com.xpto.financeiro.controllers;

import com.xpto.financeiro.dtos.CreateClientDTO;
import com.xpto.financeiro.dtos.UpdateClientDTO;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    // Endpoint para testar a chamada da function Oracle
    @GetMapping("/{id}/tarifa")
    public ResponseEntity<Double> getClientFee(@PathVariable UUID id,
            @RequestParam("start") Date start,
            @RequestParam("end") Date end) {
        Double tarifa = clientService.calcularTarifaCliente(id, start, end);
        return ResponseEntity.ok(tarifa);
    }

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable UUID id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody CreateClientDTO dto) {
        Client client = clientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable UUID id, @Valid @RequestBody UpdateClientDTO dto) {
        Client client = clientService.update(id, dto);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
