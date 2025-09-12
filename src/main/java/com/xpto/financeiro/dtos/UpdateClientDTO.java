package com.xpto.financeiro.dtos;

import javax.validation.constraints.NotBlank;

public class UpdateClientDTO {
    private String name;
    private String cellPhone;
    private String cpf;
    private String cnpj;

    // Construtores
    public UpdateClientDTO() {}

    public UpdateClientDTO(String name, String cellPhone, String cpf, String cnpj) {
        this.name = name;
        this.cellPhone = cellPhone;
        this.cpf = cpf;
        this.cnpj = cnpj;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
