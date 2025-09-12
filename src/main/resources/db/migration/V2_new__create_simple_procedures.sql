-- V2__create_procedures.sql
-- Procedures e funções para relatórios baseadas na nova estrutura

-- Procedure para obter saldo de todos os clientes
CREATE OR REPLACE PROCEDURE GetClientBalance
AS
BEGIN
    -- Esta procedure será implementada após a nova estrutura estar criada
    NULL;
END;
/

-- Procedure para obter saldo de um cliente específico
CREATE OR REPLACE PROCEDURE GetClientBalanceById
(p_client_id IN RAW)
AS
BEGIN
    -- Esta procedure será implementada após a nova estrutura estar criada
    NULL;
END;
/

-- Procedure para obter detalhes de saldo de um cliente
CREATE OR REPLACE PROCEDURE GetClientBalanceDetail
(p_client_id IN RAW)
AS
BEGIN
    -- Esta procedure será implementada após a nova estrutura estar criada
    NULL;
END;
/

-- Procedure para obter informações de transações de um cliente
CREATE OR REPLACE PROCEDURE GetClientTransactionInfo
(p_client_id IN RAW)
AS
BEGIN
    -- Esta procedure será implementada após a nova estrutura estar criada
    NULL;
END;
/

-- Trigger para atualizar saldo da conta após inserção de transação
CREATE OR REPLACE TRIGGER tr_update_account_balance
    AFTER
INSERT ON transaction
    FOR
EACH
ROW
DECLARE
    v_current_balance NUMBER
(15,2);
BEGIN
    -- Buscar saldo atual da conta
    SELECT balance
    INTO v_current_balance
    FROM account
    WHERE id = :NEW.account_id;

    -- Atualizar saldo baseado no tipo de operação
    IF :NEW.operation_type = 'C' THEN
        v_current_balance := v_current_balance + :NEW.amount;
ELSIF :NEW.operation_type = 'D' THEN
        v_current_balance := v_current_balance - :NEW.amount;
END
IF;
    
    -- Atualizar o saldo na conta
    UPDATE account 
    SET balance = v_current_balance,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = :NEW.account_id;
END;
/

COMMIT;
