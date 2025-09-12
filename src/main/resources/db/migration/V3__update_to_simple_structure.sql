-- V3__update_to_simple_structure.sql
-- Atualização para estrutura simplificada baseada no repositório de referência

-- Primeiro, vamos dropar as tabelas da estrutura anterior (se existirem)
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE movimentacao CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE conta CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE endereco CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE pessoa_fisica CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE pessoa_juridica CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE cliente CASCADE CONSTRAINTS';
    EXCEPTION
   WHEN OTHERS THEN NULL;
END;
/

-- Criar nova estrutura simplificada
-- Tabela Client (estrutura simplificada)
CREATE TABLE client
(
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    cell_phone VARCHAR2(20),
    client_type VARCHAR2(2) NOT NULL,
    cpf VARCHAR2(11),
    cnpj VARCHAR2(14),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT ck_client_type CHECK (client_type IN ('PF', 'PJ')),
    CONSTRAINT ck_client_cpf_cnpj CHECK (
        (client_type = 'PF' AND cpf IS NOT NULL AND cnpj IS NULL) OR
        (client_type = 'PJ' AND cnpj IS NOT NULL AND cpf IS NULL)
    )
);

-- Tabela Address (relacionamento OneToOne com Client)
CREATE TABLE address
(
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    address_number VARCHAR2(10),
    street VARCHAR2(255) NOT NULL,
    neighborhood VARCHAR2(100) NOT NULL,
    city VARCHAR2(100) NOT NULL,
    state VARCHAR2(2) NOT NULL,
    zip_code VARCHAR2(8) NOT NULL,
    client_id RAW(16) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_address_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT uk_address_client UNIQUE (client_id)
);

-- Tabela Account
CREATE TABLE account
(
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    account_number VARCHAR2(20) NOT NULL UNIQUE,
    balance NUMBER(15,2) DEFAULT 0 NOT NULL,
    initial_balance NUMBER(15,2) DEFAULT 0 NOT NULL,
    client_id RAW(16) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES client(id)
);

-- Tabela Transaction
CREATE TABLE transaction
(
    id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY,
    amount NUMBER(15,2) NOT NULL,
    operation_type VARCHAR2(1) NOT NULL,
    account_id RAW(16) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT ck_transaction_operation CHECK (operation_type IN ('C', 'D')),
    CONSTRAINT ck_transaction_amount CHECK (amount > 0)
);

-- Índices para performance
CREATE INDEX idx_client_cpf ON client(cpf);
CREATE INDEX idx_client_cnpj ON client(cnpj);
CREATE INDEX idx_address_client ON address(client_id);
CREATE INDEX idx_account_client ON account(client_id);
CREATE INDEX idx_account_number ON account(account_number);
CREATE INDEX idx_transaction_account ON transaction(account_id);
CREATE INDEX idx_transaction_created ON transaction(created_at);

COMMIT;
