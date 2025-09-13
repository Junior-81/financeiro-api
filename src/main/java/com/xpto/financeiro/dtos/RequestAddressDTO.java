package com.xpto.financeiro.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestAddressDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    private String clientId;

    @NotBlank(message = "Rua é obrigatória")
    private String street;

    @NotNull(message = "Número é obrigatório")
    private String addressNumber; // Mudando para String

    private String complement;

    @NotBlank(message = "Bairro é obrigatório")
    private String neighborhood;

    @NotBlank(message = "Cidade é obrigatória")
    private String city;

    @NotBlank(message = "Estado é obrigatório")
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    private String zipCode;

    // Getters e Setters
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getAddressNumber() { return addressNumber; }
    public void setAddressNumber(String addressNumber) { this.addressNumber = addressNumber; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
}