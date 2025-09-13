-- V1__create_tables.sql
-- Criação das tabelas do sistema financeiro

BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE cliente_seq';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;
/
CREATE SEQUENCE cliente_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE endereco_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE conta_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE movimentacao_seq START WITH 1 INCREMENT BY 1;

-- Tabela Cliente (pai)
CREATE TABLE cliente
(
    id NUMBER(19,0) NOT NULL,
    nome VARCHAR2(200) NOT NULL,
    email VARCHAR2(100),
    telefone VARCHAR2(20),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_atualizacao TIMESTAMP,
    saldo_inicial NUMBER(15,2) DEFAULT 0,
    ativo NUMBER(1,0) DEFAULT 1 NOT NULL,
    tipo_pessoa VARCHAR2(2) NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id),
    CONSTRAINT ck_cliente_ativo CHECK (ativo IN (0,1)),
    CONSTRAINT ck_cliente_tipo CHECK (tipo_pessoa IN ('PF','PJ'))
);

-- Tabela Pessoa Física
CREATE TABLE pessoa_fisica
(
    cliente_id NUMBER(19,0) NOT NULL,
    cpf VARCHAR2(11) NOT NULL,
    data_nascimento DATE,
    rg VARCHAR2(20),
    CONSTRAINT pk_pessoa_fisica PRIMARY KEY (cliente_id),
    CONSTRAINT fk_pf_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT uk_pf_cpf UNIQUE (cpf)
);

-- Tabela Pessoa Jurídica
CREATE TABLE pessoa_juridica
(
    cliente_id NUMBER(19,0) NOT NULL,
    cnpj VARCHAR2(14) NOT NULL,
    razao_social VARCHAR2(200) NOT NULL,
    nome_fantasia VARCHAR2(200),
    inscricao_estadual VARCHAR2(20),
    CONSTRAINT pk_pessoa_juridica PRIMARY KEY (cliente_id),
    CONSTRAINT fk_pj_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT uk_pj_cnpj UNIQUE (cnpj)
);

-- Tabela Endereço
CREATE TABLE endereco
(
    id NUMBER(19,0) NOT NULL,
    cep VARCHAR2(8) NOT NULL,
    logradouro VARCHAR2(200) NOT NULL,
    numero VARCHAR2(10),
    complemento VARCHAR2(100),
    bairro VARCHAR2(100) NOT NULL,
    cidade VARCHAR2(100) NOT NULL,
    uf VARCHAR2(2) NOT NULL,
    cliente_id NUMBER(19,0) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    ativo NUMBER(1,0) DEFAULT 1 NOT NULL,
    CONSTRAINT pk_endereco PRIMARY KEY (id),
    CONSTRAINT fk_endereco_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT ck_endereco_ativo CHECK (ativo IN (0,1))
);

-- Tabela Conta
CREATE TABLE conta
(
    id NUMBER(19,0) NOT NULL,
    numero VARCHAR2(20) NOT NULL,
    agencia VARCHAR2(10) NOT NULL,
    instituicao_financeira VARCHAR2(100) NOT NULL,
    tipo_conta VARCHAR2(20) NOT NULL,
    saldo_atual NUMBER(15,2) DEFAULT 0,
    cliente_id NUMBER(19,0) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    ativo NUMBER(1,0) DEFAULT 1 NOT NULL,
    CONSTRAINT pk_conta PRIMARY KEY (id),
    CONSTRAINT fk_conta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT uk_conta_numero_agencia UNIQUE (numero, agencia),
    CONSTRAINT ck_conta_ativo CHECK (ativo IN (0,1))
);

-- Tabela Movimentação
CREATE TABLE movimentacao
(
    id NUMBER(19,0) NOT NULL,
    tipo VARCHAR2(7) NOT NULL,
    valor NUMBER(15,2) NOT NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    descricao VARCHAR2(500),
    saldo_anterior NUMBER(15,2),
    saldo_posterior NUMBER(15,2),
    conta_id NUMBER(19,0) NOT NULL,
    processado NUMBER(1,0) DEFAULT 0 NOT NULL,
    CONSTRAINT pk_movimentacao PRIMARY KEY (id),
    CONSTRAINT fk_movimentacao_conta FOREIGN KEY (conta_id) REFERENCES conta(id),
    CONSTRAINT ck_movimentacao_tipo CHECK (tipo IN ('CREDITO','DEBITO')),
    CONSTRAINT ck_movimentacao_processado CHECK (processado IN (0,1)),
    CONSTRAINT ck_movimentacao_valor CHECK (valor > 0)
);

-- Índices para performance
CREATE INDEX idx_cliente_nome ON cliente(nome);
CREATE INDEX idx_cliente_ativo ON cliente(ativo);
CREATE INDEX idx_pj_cnpj ON pessoa_juridica(cnpj);
CREATE INDEX idx_endereco_cliente ON endereco(cliente_id);
CREATE INDEX idx_endereco_cep ON endereco(cep);
CREATE INDEX idx_conta_cliente ON conta(cliente_id);
CREATE INDEX idx_conta_numero ON conta(numero);
CREATE INDEX idx_movimentacao_conta ON movimentacao(conta_id);
CREATE INDEX idx_movimentacao_data ON movimentacao(data_movimentacao);
CREATE INDEX idx_movimentacao_tipo ON movimentacao(tipo);

COMMIT;
