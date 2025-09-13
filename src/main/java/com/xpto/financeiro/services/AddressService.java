
package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.RequestAddressDTO;
import com.xpto.financeiro.exceptions.ResourceNotFoundException;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Address;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.repositories.AddressRepository;
import com.xpto.financeiro.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Address create(RequestAddressDTO dto) {
        UUID clientUUID = UUID.fromString(dto.getClientId());
        Client client = clientRepository.findById(clientUUID)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Address address = new Address();
        address.setClient(client);
        address.setStreet(dto.getStreet());
        address.setAddressNumber(dto.getAddressNumber() != null ? Long.valueOf(dto.getAddressNumber()) : null);
        address.setNeighborhood(dto.getNeighborhood());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setZipCode(dto.getZipCode());

        return addressRepository.save(address);
    }

    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    public Address findById(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
    }

    public List<Address> findByClientId(UUID clientId) {
        return addressRepository.findByClientId(clientId);
    }

    public Address update(UUID id, RequestAddressDTO dto) {
        Address existingAddress = findById(id);

        existingAddress.setStreet(dto.getStreet());
        existingAddress.setAddressNumber(dto.getAddressNumber() != null ? Long.valueOf(dto.getAddressNumber()) : null);
        existingAddress.setNeighborhood(dto.getNeighborhood());
        existingAddress.setCity(dto.getCity());
        existingAddress.setState(dto.getState());
        existingAddress.setZipCode(dto.getZipCode());

        return addressRepository.save(existingAddress);
    }

    public void delete(UUID id) {
        Address address = findById(id);
        addressRepository.delete(address);
    }
}