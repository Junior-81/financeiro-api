package com.xpto.financeiro.controllers;

import com.xpto.financeiro.dtos.RequestAddressDTO;
import com.xpto.financeiro.models.Address;
import com.xpto.financeiro.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> create(@Valid @RequestBody RequestAddressDTO addressDTO) {
        Address savedAddress = addressService.create(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findById(@PathVariable UUID id) {
        Address address = addressService.findById(id);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Address>> findByClientId(@PathVariable UUID clientId) {
        List<Address> addresses = addressService.findByClientId(clientId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        List<Address> addresses = addressService.findAll();
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable UUID id, @Valid @RequestBody RequestAddressDTO addressDTO) {
        Address updatedAddress = addressService.update(id, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}