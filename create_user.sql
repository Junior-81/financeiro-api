-- execute como SYSTEM no Oracle SQL Developer ou sqlplus
-- sqlplus system/oracle@localhost:1521/XE

-- Criar usuário para o projeto
CREATE USER xpto_financeiro
IDENTIFIED BY xpto123;

-- Conceder privilégios
GRANT CONNECT, RESOURCE TO xpto_financeiro;
GRANT CREATE SESSION TO xpto_financeiro;
GRANT CREATE TABLE TO xpto_financeiro;
GRANT CREATE SEQUENCE TO xpto_financeiro;
GRANT CREATE PROCEDURE TO xpto_financeiro;
GRANT CREATE TRIGGER TO xpto_financeiro;
GRANT CREATE VIEW TO xpto_financeiro;

-- Quota no tablespace
ALTER USER xpto_financeiro QUOTA UNLIMITED ON USERS;

-- Verificar se foi criado
SELECT username
FROM dba_users
WHERE username = 'XPTO_FINANCEIRO';

COMMIT;

-- Para testar:
-- CONNECT xpto_financeiro/xpto123;
