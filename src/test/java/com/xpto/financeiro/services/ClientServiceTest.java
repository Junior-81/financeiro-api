package com.xpto.financeiro.services;

import com.xpto.financeiro.dtos.CreateClientDTO;
import com.xpto.financeiro.dtos.RequestAddressDTO;
import com.xpto.financeiro.exceptions.ValidationException;
import com.xpto.financeiro.models.Client;
import com.xpto.financeiro.models.ClientType;
import com.xpto.financeiro.repositories.AccountRepository;
import com.xpto.financeiro.repositories.AddressRepository;
import com.xpto.financeiro.repositories.ClientRepository;
import com.xpto.financeiro.repositories.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ClientService clientService;

    private CreateClientDTO validPFClientDTO;
    private CreateClientDTO validPJClientDTO;
    private CreateClientDTO invalidClientDTO;

    @Before
    public void setUp() {
        validPFClientDTO = new CreateClientDTO();
        validPFClientDTO.setName("João Silva");
        validPFClientDTO.setEmail("joao@teste.com");
        validPFClientDTO.setPhone("11987654321");
        validPFClientDTO.setCellPhone("11987654321");
        validPFClientDTO.setCpf("12345678901");
        validPFClientDTO.setClientType(ClientType.INDIVIDUAL);
        validPFClientDTO.setInitialBalance(BigDecimal.valueOf(1000.00));

        RequestAddressDTO addressDTO = new RequestAddressDTO();
        addressDTO.setStreet("Rua das Flores");
        addressDTO.setAddressNumber("123");
        addressDTO.setNeighborhood("Centro");
        addressDTO.setCity("São Paulo");
        addressDTO.setState("SP");
        addressDTO.setZipCode("01234567");
        validPFClientDTO.setAddress(addressDTO);

        
        validPJClientDTO = new CreateClientDTO();
        validPJClientDTO.setName("Empresa XYZ Ltda");
        validPJClientDTO.setEmail("contato@empresa.com");
        validPJClientDTO.setPhone("1134567890");
        validPJClientDTO.setCellPhone("11987654321");
        validPJClientDTO.setCnpj("12345678000123");
        validPJClientDTO.setClientType(ClientType.CORPORATE);
        validPJClientDTO.setInitialBalance(BigDecimal.valueOf(5000.00));
        validPJClientDTO.setAddress(addressDTO);

        
        invalidClientDTO = new CreateClientDTO();
        invalidClientDTO.setName(""); 
        invalidClientDTO.setEmail("email-invalido");
        invalidClientDTO.setClientType(ClientType.INDIVIDUAL);
    }

    @Test
    public void testCreateValidPFClient() {
        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID());
        savedClient.setName(validPFClientDTO.getName());
        savedClient.setEmail(validPFClientDTO.getEmail());

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(accountRepository.save(any())).thenReturn(null);
        when(addressRepository.save(any())).thenReturn(null);
        when(transactionRepository.save(any())).thenReturn(null);

        
        Client result = clientService.create(validPFClientDTO);

        
        assertNotNull(result);
        assertEquals(validPFClientDTO.getName(), result.getName());
        assertEquals(validPFClientDTO.getEmail(), result.getEmail());
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(accountRepository, times(1)).save(any());
        verify(addressRepository, times(1)).save(any());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    public void testCreateValidPJClient() {
        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID());
        savedClient.setName(validPJClientDTO.getName());
        savedClient.setEmail(validPJClientDTO.getEmail());

        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(accountRepository.save(any())).thenReturn(null);
        when(addressRepository.save(any())).thenReturn(null);
        when(transactionRepository.save(any())).thenReturn(null);


        Client result = clientService.create(validPJClientDTO);

 
        assertNotNull(result);
        assertEquals(validPJClientDTO.getName(), result.getName());
        assertEquals(validPJClientDTO.getEmail(), result.getEmail());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientWithEmptyName() {

        clientService.create(invalidClientDTO);
    }

    @Test(expected = ValidationException.class)
    public void testCreatePFClientWithoutCPF() {

        validPFClientDTO.setCpf(null);


        clientService.create(validPFClientDTO);
    }

    @Test(expected = ValidationException.class)
    public void testCreatePJClientWithoutCNPJ() {
        validPJClientDTO.setCnpj(null);


        clientService.create(validPJClientDTO);
    }

    @Test(expected = ValidationException.class)
    public void testCreateClientWithInvalidEmail() {
        validPFClientDTO.setEmail("email-invalido");

        clientService.create(validPFClientDTO);
    }

    @Test
    public void testFindById() {
        UUID clientId = UUID.randomUUID();
        Client client = new Client();
        client.setId(clientId);
        client.setName("João Silva");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));


        Client result = clientService.findById(clientId);


        assertNotNull(result);
        assertEquals(clientId, result.getId());
        assertEquals("João Silva", result.getName());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    public void testFindAll() {
        Client client1 = new Client();
        client1.setName("João Silva");
        Client client2 = new Client();
        client2.setName("Maria Santos");

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));


        java.util.List<Client> result = clientService.findAll();


        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void testValidateCPF() {
        assertTrue("CPF válido deve retornar true",
                isValidCPF("12345678901"));


        assertFalse("CPF com letras deve retornar false",
                isValidCPF("123abc78901"));

        assertFalse("CPF com tamanho errado deve retornar false",
                isValidCPF("123456789"));
    }

    @Test
    public void testValidateCNPJ() {
        assertTrue("CNPJ válido deve retornar true",
                isValidCNPJ("12345678000123"));


        assertFalse("CNPJ com letras deve retornar false",
                isValidCNPJ("123abc78000123"));

        assertFalse("CNPJ com tamanho errado deve retornar false",
                isValidCNPJ("12345678000"));
    }


    private boolean isValidCPF(String cpf) {
        if (cpf == null)
            return false;
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11 && cpf.matches("\\d{11}");
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj == null)
            return false;
        cnpj = cnpj.replaceAll("[^0-9]", "");
        return cnpj.length() == 14 && cnpj.matches("\\d{14}");
    }
}