-- V2__create_procedures_and_functions.sql
-- Criação de procedures e functions PL/SQL conforme exigido no desafio

-- Function para calcular tarifa por cliente e período (conforme regras do desafio)
CREATE OR REPLACE FUNCTION calculate_client_fee
(
    p_client_id IN RAW,
    p_start_date IN DATE,
    p_end_date IN DATE
)
RETURN NUMBER
IS
    v_transaction_count NUMBER;
    v_fee NUMBER := 0;
BEGIN
    -- Contar transações do cliente no período
    SELECT COUNT(*)
    INTO v_transaction_count
    FROM transactions t
        JOIN account a ON t.account_id = a.id
    WHERE a.client_id = p_client_id
        AND t.created_at BETWEEN p_start_date AND p_end_date;

    -- Calcular tarifa baseada nas regras:
    -- Até 10 movimentações: R$ 1,00 por movimentação
    -- De 10 a 20 movimentações: R$ 0,75 por movimentação
    -- Acima de 20 movimentações: R$ 0,50 por movimentação

    IF v_transaction_count <= 10 THEN
        v_fee := v_transaction_count * 1.00;
ELSIF v_transaction_count <= 20 THEN
        v_fee :=
(10 * 1.00) +
((v_transaction_count - 10) * 0.75);
    ELSE
        v_fee :=
(10 * 1.00) +
(10 * 0.75) +
((v_transaction_count - 20) * 0.50);
END
IF;
    
    RETURN v_fee;
EXCEPTION
    WHEN OTHERS THEN
RETURN 0;
END calculate_client_fee;
/

-- Procedure para obter relatório de saldo do cliente
CREATE OR REPLACE PROCEDURE get_client_balance_report
(
    p_client_id IN RAW,
    p_start_date IN DATE DEFAULT NULL,
    p_end_date IN DATE DEFAULT NULL,
    p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cursor
    FOR
    SELECT
        c.id,
        c.name,
        c.client_type,
        c.cpf,
        c.cnpj,
        c.created_at as client_since,
        a.street,
        a.address_number,
        a.neighborhood,
        a.city,
        a.state,
        a.zip_code,
        acc.initial_balance,
        acc.balance as current_balance,
        (SELECT COUNT(*)
        FROM transactions t
        WHERE t.account_id = acc.id
            AND t.operation_type = 'C'
            AND (p_start_date IS NULL OR t.created_at >= p_start_date)
            AND (p_end_date IS NULL OR t.created_at <= p_end_date)) as credit_count,
        (SELECT COUNT(*)
        FROM transactions t
        WHERE t.account_id = acc.id
            AND t.operation_type = 'D'
            AND (p_start_date IS NULL OR t.created_at >= p_start_date)
            AND (p_end_date IS NULL OR t.created_at <= p_end_date)) as debit_count,
        (SELECT COUNT(*)
        FROM transactions t
        WHERE t.account_id = acc.id
            AND (p_start_date IS NULL OR t.created_at >= p_start_date)
            AND (p_end_date IS NULL OR t.created_at <= p_end_date)) as total_transactions,
        calculate_client_fee(c.id, 
                COALESCE(p_start_date, c.created_at), 
                COALESCE(p_end_date, SYSDATE)) as fee_amount
    FROM client c
        LEFT JOIN address a ON c.id = a.client_id
        LEFT JOIN account acc ON c.id = acc.client_id
    WHERE c.id = p_client_id;
END
get_client_balance_report;
/

-- Procedure para obter relatório de todos os clientes
CREATE OR REPLACE PROCEDURE get_all_clients_balance
(
    p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cursor
    FOR
    SELECT
        c.id,
        c.name,
        c.client_type,
        c.created_at as client_since,
        SUM(acc.balance) as total_balance,
        COUNT(acc.id) as account_count
    FROM client c
        LEFT JOIN account acc ON c.id = acc.client_id
    WHERE acc.active = 1 OR acc.active IS NULL
    GROUP BY c.id, c.name, c.client_type, c.created_at
    ORDER BY c.name;
END
get_all_clients_balance;
/

-- Procedure para relatório de receita da empresa por período
CREATE OR REPLACE PROCEDURE get_company_revenue_report
(
    p_start_date IN DATE,
    p_end_date IN DATE,
    p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cursor
    FOR
    SELECT
        c.id,
        c.name,
        COUNT(t.id) as transaction_count,
        calculate_client_fee(c.id, p_start_date, p_end_date) as fee_amount
    FROM client c
        LEFT JOIN account acc ON c.id = acc.client_id
        LEFT JOIN transactions t ON acc.id = t.account_id
    WHERE t.created_at BETWEEN p_start_date AND p_end_date
    GROUP BY c.id, c.name
    HAVING COUNT(t.id) > 0
    ORDER BY fee_amount DESC;
END
get_company_revenue_report;
/

-- Trigger para atualizar saldo da conta automaticamente
CREATE OR REPLACE TRIGGER tr_update_account_balance
    AFTER
INSERT ON
transactions
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
END tr_update_account_balance;
/

-- Trigger para atualizar timestamps automaticamente
CREATE OR REPLACE TRIGGER tr_client_updated
    BEFORE
UPDATE ON client
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END
tr_client_updated;
/

CREATE OR REPLACE TRIGGER tr_address_updated
    BEFORE
UPDATE ON address
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END
tr_address_updated;
/

CREATE OR REPLACE TRIGGER tr_account_updated
    BEFORE
UPDATE ON account
    FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END
tr_account_updated;
/

COMMIT;
