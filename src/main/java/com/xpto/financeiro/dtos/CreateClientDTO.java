package com.xpto.financeiro.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class CreateClientDTO {
    @NotNull
    private String name;
    
    @NotNull 
    @Size(min = 11, max = 11, message = "O campo deve ter 11 dígitos")
    private String cellPhone;
    
    @NotNull 
    @Pattern(regexp = "\\d{11}|\\d{14}", message = "O campo deve ter 11 ou 14 dígitos")
    private String document;
    
    @NotNull 
    @Pattern(regexp = "PF|PJ", message = "O campo deve ser 'PF' ou 'PJ'")
    private String clientType;
    
    @NotNull 
    @Valid
    private RequestAddressDTO address;
    
    @NotNull
    private String accountNumber;
    
    @NotNull
    private BigDecimal initialBalance;

    // Construtores
    public CreateClientDTO() {}

    public CreateClientDTO(String name, String cellPhone, String document, String clientType, 
                          RequestAddressDTO address, String accountNumber, BigDecimal initialBalance) {
        this.name = name;
        this.cellPhone = cellPhone;
        this.document = document;
        this.clientType = clientType;
        this.address = address;
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public RequestAddressDTO getAddress() {
        return address;
    }

    public void setAddress(RequestAddressDTO address) {
        this.address = address;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
