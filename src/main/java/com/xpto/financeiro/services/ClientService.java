package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.CreateClientDTO;
import com.xpto.financeiro.dtos.UpdateClientDTO;
import com.xpto.financeiro.exceptions.ResourceNotFoundException;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Account;
import com.xpto.financeiro.models.Address;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.models.ClientType;
import com.xpto.financeiro.models.Transaction;
import com.xpto.financeiro.repositories.AccountRepository;
import com.xpto.financeiro.repositories.AddressRepository;
import com.xpto.financeiro.repositories.ClientRepository;
import com.xpto.financeiro.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.Types;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Chama a function Oracle 'calculate_client_fee' para calcular a tarifa de um
     * cliente em um período.
     * 
     * @param clientId  UUID do cliente
     * @param startDate java.sql.Date início
     * @param endDate   java.sql.Date fim
     * @return valor da tarifa calculada
     */
    public Double calcularTarifaCliente(UUID clientId, Date startDate, Date endDate) {
        Double tarifa = 0.0;
        try {
            Connection conn = entityManager.unwrap(Connection.class);
            CallableStatement stmt = conn.prepareCall("{ ? = call calculate_client_fee(?, ?, ?) }");
            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.setObject(2, clientId, Types.VARCHAR);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            stmt.execute();
            tarifa = stmt.getDouble(1);
            stmt.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular tarifa do cliente via Oracle", e);
        }
        return tarifa;
    }

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

        
        client = clientRepository.save(client);

        
        Account initialAccount = new Account();
        initialAccount.setClient(client);
        initialAccount.setAccountNumber(generateAccountNumber(client.getId()));
        initialAccount.setBalance(BigDecimal.ZERO);
        initialAccount.setCreatedAt(LocalDateTime.now());
        initialAccount = accountRepository.save(initialAccount);

       
        if (dto.getInitialBalance() != null && dto.getInitialBalance().compareTo(BigDecimal.ZERO) > 0) {
            Transaction initialTransaction = new Transaction();
            initialTransaction.setAccount(initialAccount);
            initialTransaction.setAmount(dto.getInitialBalance());
            initialTransaction.setOperationType(Transaction.OperationType.C); 
            initialTransaction.setCreatedAt(LocalDateTime.now());
            transactionRepository.save(initialTransaction);

            
            initialAccount.setBalance(dto.getInitialBalance());
            accountRepository.save(initialAccount);
        }

        return client;
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

    private String generateAccountNumber(UUID clientId) {
        return clientId.toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
