-- Script de configuração inicial do Oracle para o projeto XPTO Financeiro
-- Execute este script como usuário SYSTEM no Oracle

-- Criar usuário específico para o projeto
CREATE USER xpto_financeiro
IDENTIFIED BY xpto123;

-- Conceder privilégios necessários
GRANT CONNECT, RESOURCE TO xpto_financeiro;
GRANT CREATE SESSION TO xpto_financeiro;
GRANT CREATE TABLE TO xpto_financeiro;
GRANT CREATE SEQUENCE TO xpto_financeiro;
GRANT CREATE PROCEDURE TO xpto_financeiro;
GRANT CREATE TRIGGER TO xpto_financeiro;
GRANT CREATE VIEW TO xpto_financeiro;

-- Conceder quota no tablespace
ALTER USER xpto_financeiro QUOTA UNLIMITED ON USERS;

-- Conectar como o novo usuário para verificar
-- CONNECT xpto_financeiro/xpto123;

COMMIT;
