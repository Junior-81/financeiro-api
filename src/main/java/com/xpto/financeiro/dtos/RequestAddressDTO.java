package com.xpto.financeiro.dtos;

import javax.validation.constraints.NotNull;

public class RequestAddressDTO {
    private Long addressNumber;
    
    @NotNull
    private String street;
    
    @NotNull
    private String neighborhood;
    
    @NotNull
    private String city;
    
    @NotNull
    private String state;
    
    @NotNull
    private String zipCode;

    // Construtores
    public RequestAddressDTO() {}

    public RequestAddressDTO(Long addressNumber, String street, String neighborhood, 
                           String city, String state, String zipCode) {
        this.addressNumber = addressNumber;
        this.street = street;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    // Getters e Setters
    public Long getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Long addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
