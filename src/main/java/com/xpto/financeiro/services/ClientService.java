package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.CreateClientDTO;
import com.xpto.financeiro.dtos.UpdateClientDTO;
import com.xpto.financeiro.exceptions.ResourceNotFoundException;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.models.ClientType;
import com.xpto.financeiro.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(UUID id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    public Client create(CreateClientDTO dto) {
        validateClientData(dto);
        
        Client client = new Client();
        client.setName(dto.getName());
        client.setCellPhone(dto.getCellPhone());
        client.setClientType(dto.getClientType());
        client.setCpf(dto.getCpf());
        client.setCnpj(dto.getCnpj());
        client.setCreatedAt(LocalDateTime.now());
        
        return clientRepository.save(client);
    }

    public Client update(UUID id, UpdateClientDTO dto) {
        Client client = findById(id);
        
        if (dto.getName() != null) {
            client.setName(dto.getName());
        }
        if (dto.getCellPhone() != null) {
            client.setCellPhone(dto.getCellPhone());
        }
        if (dto.getCpf() != null) {
            client.setCpf(dto.getCpf());
        }
        if (dto.getCnpj() != null) {
            client.setCnpj(dto.getCnpj());
        }
        
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    public void delete(UUID id) {
        Client client = findById(id);
        clientRepository.delete(client);
    }

    private void validateClientData(CreateClientDTO dto) {
        if (dto.getClientType() == ClientType.INDIVIDUAL) {
            if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
                throw new ValidationException("CPF é obrigatório para Pessoa Física");
            }
            if (clientRepository.existsByCpf(dto.getCpf())) {
                throw new ValidationException("CPF já cadastrado");
            }
        } else if (dto.getClientType() == ClientType.CORPORATE) {
            if (dto.getCnpj() == null || dto.getCnpj().trim().isEmpty()) {
                throw new ValidationException("CNPJ é obrigatório para Pessoa Jurídica");
            }
            if (clientRepository.existsByCnpj(dto.getCnpj())) {
                throw new ValidationException("CNPJ já cadastrado");
            }
        }
    }
}
