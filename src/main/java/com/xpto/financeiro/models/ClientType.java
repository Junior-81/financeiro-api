package com.xpto.financeiro.models;

public enum ClientType {
    INDIVIDUAL("Individual"),
    CORPORATE("Corporate");

    private final String description;

    ClientType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}